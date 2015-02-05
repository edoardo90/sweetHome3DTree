package com.eteks.sweethome3d.adaptive.security.ifcSecurity;

import ifc2x3javatoolbox.ifc2x3tc1.DOUBLE;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement2D;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement3D;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDirection;
import ifc2x3javatoolbox.ifc2x3tc1.IfcElement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcExtrudedAreaSolid;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLengthMeasure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLocalPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcObjectDefinition;
import ifc2x3javatoolbox.ifc2x3tc1.IfcObjectPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProductDefinitionShape;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProductRepresentation;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProfileDef;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRectangleProfileDef;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelContainedInSpatialStructure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelDecomposes;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelSpaceBoundary;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRepresentation;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRepresentationItem;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSIPrefix;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSIUnit;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSIUnitName;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSpace;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;
import ifc2x3javatoolbox.ifc2x3tc1.SET;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingLinkEdge;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingRoomNode;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.UnknownObject;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Axis3DNice;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Placement3DNice;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Rectangle3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Shape3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.UserPreferences;

public class IfcSecurityExtractor {

  private String ifcFileName;
  private IfcModel ifcModel;
  private List<IfcSpace> ifcSpaces = new ArrayList<IfcSpace>();
  
  public IfcSecurityExtractor(String ifcFileName, UserPreferences preferences)
  {
    this.ifcFileName = ifcFileName;
    
    ConfigLoader configLoader = new ConfigLoader(preferences);
    Map<BuildingObjectType, HomePieceOfFurniture> map =
            configLoader.createFurnitureMap();
    preferences.setFornitureMap(map); 
  }


  public BuildingSecurityGraph getGraphFromFile() throws Exception
  {
    BuildingSecurityGraph buildingSecurityGraph = new BuildingSecurityGraph();

    List<BuildingLinkEdge> linkEdgeList;
    List<BuildingRoomNode> roomNodeList;

    ifcModel = new IfcModel();

    File stepFile = new File(this.ifcFileName);

    ifcModel.readStepFile(stepFile);

    Collection<IfcSpace> ifcSpacesColl = ifcModel.getCollection(IfcSpace.class);

    this.ifcSpaces.addAll(ifcSpacesColl);
    
    float scaleFactor = this.getScaleFactor();

    linkEdgeList = this.getLinks();
    roomNodeList = this.getRooms(scaleFactor);

    buildingSecurityGraph.setLinkEdgeList(linkEdgeList);
    buildingSecurityGraph.setRoomNodeList(roomNodeList);

    return buildingSecurityGraph;
  }
  
  /**
   * In sweethome 3d objects are represented with dimensions in cm
   * so if we recognize that the ifc file have sizes expressed in mm or in m 
   * we have to scale objects accordingly
   * 
   * SwOBJ =  IFC OBJ  *  scaleFactor
   * 
   * IFC obj:  500 mm   ->  SW3D obj:  50 cm
   * so   mm  to  cm    ->  scaleFactor = 0.10
   * 
   * IFC obj:    3 m    ->   SW3D obj  300 cm
   * so   m   to  cm    ->   scaleFactor = 100
   * 
   * @return
   */
  private float getScaleFactor()
  {
    //TODO: now are recognized just mm vs m 
    //maybe we can recognize imperial system as well
    float scaleF = 100f;
    Collection<IfcSIUnit> collectionOfUnit = ifcModel.getCollection(IfcSIUnit.class);
    
    //if the ifc file is expressed in mm  then  both  metre and millimetre units will be present
    //inside the file
    for(IfcSIUnit unit : collectionOfUnit)
    {
      IfcSIPrefix prefix = unit.getPrefix();
      IfcSIUnitName nameOfUnit = unit.getName();
      
      String pr="";
      if(prefix != null)
      {
        pr = "" + prefix.value;
      }
      String un="";
      if(nameOfUnit != null)
        un = "" + nameOfUnit.value;
      
      System.out.println(" PREF:" + pr +  "UN:" + un);
      if(pr.equals("MILLI") &&  un.equals("METRE"))
      {
        scaleF = 0.10f;
      }
      
      
    }
    return scaleF;
  }
  

  private List<BuildingLinkEdge> getLinks()
  {
    List<BuildingLinkEdge> buildingLinkEdgeList = new ArrayList<BuildingLinkEdge>();

    // for all rooms
    for(IfcSpace spaceToTest : this.ifcSpaces)
    {

      SET<IfcRelSpaceBoundary> ifcRelSpaceBounds = spaceToTest.getBoundedBy_Inverse();
      Iterator<IfcRelSpaceBoundary> iterRelSpace = ifcRelSpaceBounds.iterator();

      //for all walls attached to the room
      while(iterRelSpace.hasNext())
      {
        IfcRelSpaceBoundary ifcRelSpaceBound = iterRelSpace.next();
        IfcElement elementBounding = ifcRelSpaceBound.getRelatedBuildingElement(); //e.g. a wall

        if(elementBounding != null)
        {

          SET<IfcRelSpaceBoundary> setOfSpacesRelsThatAreBounded = elementBounding.getProvidesBoundaries_Inverse();
          Iterator<IfcRelSpaceBoundary> iteratorOfSpacesRel = setOfSpacesRelsThatAreBounded.iterator();

          //we look at the rooms related to that wall
          while(iteratorOfSpacesRel.hasNext())
          {
            IfcRelSpaceBoundary relSpaceBound = iteratorOfSpacesRel.next();
            IfcSpace relatingSpace = relSpaceBound.getRelatingSpace();

            BuildingLinkEdge buildingEdge = new 
                BuildingLinkEdge(null, relatingSpace.getGlobalId().getDecodedValue(),
                    spaceToTest.getGlobalId().getDecodedValue());

            if(buildingEdge.makeSense())
            {
              buildingLinkEdgeList.add(buildingEdge);

              String longNameFirs = spaceToTest.getLongName().getDecodedValue();
              String longNameSecond = relatingSpace.getLongName().getDecodedValue();


            }
          }
        }
      }
    }

    return buildingLinkEdgeList;

  }

  private List<BuildingRoomNode> getRooms()
  {
    return getRooms(1f);
  }
  
  
  private List<BuildingRoomNode> getRooms(float scaleFactor)
  {	

    List<BuildingRoomNode> buildingRoomList = new ArrayList<BuildingRoomNode>();

    IfcSpace firstRoom = ifcSpaces.get(0);
    String storeyName =  getStoreyName(firstRoom);

    for(IfcSpace space : ifcSpaces)
    {
      if(getStoreyName(space).equals(storeyName))
      {
        
        String roomName = space.getLongName().getDecodedValue();
        String idRoom = space.getGlobalId().getDecodedValue();
        System.out.println("\n _________________________________\n storey : "
            + storeyName + "\n room: " + roomName 
            + " room ID : "  + idRoom);

        //shape and position
        Shape3D roomShape = getShapeAndPosition(space, scaleFactor); 
       
       
        System.out.println("room shape: \n" + roomShape);

        //containement
        List<BuildingObjectContained> objects = getObjectsOfRoom(space, scaleFactor);
        System.out.println( "contains: " +  objects);


        BuildingRoomNode buildingRoomNode = new
            BuildingRoomNode(roomName, null, roomShape, objects);
        buildingRoomNode.setBuildingRoomParameters(idRoom);
        buildingRoomList.add(buildingRoomNode);
      }

    }
    sanitizeMinDimensions(buildingRoomList);
    return buildingRoomList;

  }

  private String getStoreyName(IfcSpace firstRoom) {

    SET<IfcRelDecomposes> decInv = firstRoom.getDecomposes_Inverse();
    Iterator<IfcRelDecomposes> decInvIter = decInv.iterator();
    IfcRelDecomposes relDecomp = decInvIter.next();

    IfcObjectDefinition rob = relDecomp.getRelatingObject();
    return rob.getGlobalId().getDecodedValue();

  }



  private void sanitizeMinDimensions(List<BuildingRoomNode> buildingRoomList)
  {

  }

  

  private List<BuildingObjectContained> getObjectsOfRoom(IfcSpace space, float scalePositionFactor) {

    List<BuildingObjectContained> contained = new ArrayList<BuildingObjectContained>();
    SET<IfcRelContainedInSpatialStructure> ifcRelContainedInSpatialStructure =  space.getContainsElements_Inverse();
    if(ifcRelContainedInSpatialStructure != null)
    {
      Iterator<IfcRelContainedInSpatialStructure> iterIFCRel = ifcRelContainedInSpatialStructure.iterator();

      while(iterIFCRel.hasNext())
      {
        IfcRelContainedInSpatialStructure ifcRelContained =  iterIFCRel.next();
        SET<IfcProduct> setOfProductsContained = ifcRelContained.getRelatedElements();
        Iterator<IfcProduct> iteratorProductContained = setOfProductsContained.iterator();
        while(iteratorProductContained.hasNext())
        {
          IfcProduct furnitureProduct = iteratorProductContained.next();
          Vector3D furniturePosition = getPositionOfProduct(furnitureProduct);
          
          //scale to match length unit used in the ifc file
          furniturePosition.scale(scalePositionFactor);
          BuildingObjectContained singleFurniture = getObectContained( furniturePosition, furnitureProduct);

         
          if(! (singleFurniture instanceof UnknownObject))
            contained.add(singleFurniture);


          String productName = furnitureProduct.getName().getDecodedValue();
          System.out.println("\t\t " + productName);
        }
      }
    }

    return contained;
  }



  /**
   * From IFC prodcut (phisical object inside a space) to security element phisical object
   * we look for:
   * light, cctv, PC, printer, hvac 
   * @param product
   * @return
   */
  private BuildingObjectContained getObectContained(Vector3D position, IfcProduct product)
  {
    String actualName = product.getName().getDecodedValue();

    for(BuildingObjectType objType : BuildingObjectType.values())
    {
      List<String> toLookStrings = ConfigLoader.stringToLookFor(objType);
      for(String nameToLookFor : toLookStrings)
      {
        if(matches(nameToLookFor, actualName))
        {
          return objType.getBuildingObjectOfType(position);
        }
      }
    }

    return new UnknownObject(null);
  }


  private boolean matches(String nameToLookFor, String actualName)
  {
    String upperLook = nameToLookFor.toUpperCase();
    String upperActual = actualName.toUpperCase();
    return (upperActual.contains("" + upperLook));
  }

  


  private Shape3D getShapeAndPosition(IfcProduct product)
  {
    return getShapeAndPosition(product, 1);
  }


  private Shape3D getShapeAndPosition(IfcProduct product, float scaleFactor) 
  {

    Placement3DNice placementNice = getPlacementFromObject(product);
    Axis3DNice axis3DContainer = placementNice.getAxes();
    Vector3D   absoluteCoordinates = placementNice.getOriginPoint();


    Shape3D shapeLocated = this.getShape(product, axis3DContainer, absoluteCoordinates);
    shapeLocated.scale(scaleFactor);
    return shapeLocated;
  }
  
  private Vector3D getPositionOfProduct(IfcProduct product) {
      return getPlacementFromObject(product).getOriginPoint();
  }
  
  

  private Placement3DNice getPlacementFromObject(IfcProduct product)
  {

    IfcObjectPlacement placement = product.getObjectPlacement();
    Axis3DNice axis3DContainer ;
    Vector3D   absoluteCoordinates;

    Deque<IfcAxis2Placement> axises = new ArrayDeque<IfcAxis2Placement>();
    boolean stillRelativePlacements = true;
    if(placement == null)
      stillRelativePlacements = false; 

    while(stillRelativePlacements)
    {
      if (placement instanceof IfcLocalPlacement)
      {
        IfcLocalPlacement localPlacement = (IfcLocalPlacement)  placement;

        IfcAxis2Placement axis = localPlacement.getRelativePlacement();

        if(axis == null)
          axis = getDefault3DAxis();

        axises.add(axis);

        placement = localPlacement.getPlacementRelTo();  //recursively !
        if(placement == null)
        {
          stillRelativePlacements = false;
          IfcAxis2Placement3D default3DAxis = getDefault3DAxis();
          axises.add(default3DAxis);
        }

      }
    }

    /**
     *    ^
     *    ^
     *    ^             ^
     *    |            ^
     *    zVector          yVector/ 
     *    |      /
     *    |    /
     *    |  /
     *    ----------------xVector---->
     *    
     * 
     */

    //read the stack of axises and updates coordinates
    axis3DContainer = new Axis3DNice();
    absoluteCoordinates = new Vector3D(0, 0, 0);
    while(!axises.isEmpty())
    {
      IfcAxis2Placement axis = axises.pollLast(); //read and remove top of stack
      if(axis instanceof  IfcAxis2Placement3D)
      {
        IfcAxis2Placement3D ifcAxis3d = (IfcAxis2Placement3D) axis;
        IfcCartesianPoint locationPoint =  ifcAxis3d.getLocation();

        Vector3D localPointNice = getPoint3DNice(locationPoint);
        //the location of the axis have to be casted according to container position system
        //the CPS (Coordinates Positions System) of the "father" have to be used
        Vector3D localPointInGlobals = axis3DContainer.getDefaultWordCoordinate(localPointNice);
        absoluteCoordinates.sumVector(localPointInGlobals);

        //update container, so the next iteration will use the CPS of this 
        axis3DContainer = getAxis3DNice(ifcAxis3d);
      }
    }
    return new Placement3DNice(axis3DContainer, absoluteCoordinates);

  }


  private Rectangle3D getShape(IfcProduct product, Axis3DNice localPlacementAxis, Vector3D localPlacementAbsoluteCoords) 
  {
    IfcProductRepresentation representation = product.getRepresentation();
    List<Vector3D> rectanglePoints = new ArrayList<Vector3D>();

    if(representation instanceof IfcProductDefinitionShape)
    {
      IfcProductDefinitionShape definitionShape = (IfcProductDefinitionShape) representation;
      LIST<IfcRepresentation> representationList = definitionShape.getRepresentations();
      IfcRepresentation shapeRepr = representationList.get(0);

      SET<IfcRepresentationItem> items = shapeRepr.getItems();
      Iterator<IfcRepresentationItem> iter = items.iterator();
      IfcRepresentationItem representItem = iter.next() ;
      if(representItem instanceof IfcExtrudedAreaSolid)
      {

        //area solid  contains  swept area

        IfcExtrudedAreaSolid areaSolid = (IfcExtrudedAreaSolid) representItem; 
        IfcAxis2Placement3D axisesAndPositionOfExtrudedAreaSolid = areaSolid.getPosition();

        Axis3DNice axisesExtrudedAreaSolidNice = getAxis3DNice(axisesAndPositionOfExtrudedAreaSolid);
        Vector3D pointOfAreaSolidNice = getPoint3DNice( axisesAndPositionOfExtrudedAreaSolid );


        IfcProfileDef sweptArea = areaSolid.getSweptArea();

        if(sweptArea instanceof IfcRectangleProfileDef)
        {
          IfcRectangleProfileDef sweptRectangle = (IfcRectangleProfileDef)sweptArea;
          double xDim = sweptRectangle.getXDim().value;
          double yDim = sweptRectangle.getYDim().value;

          IfcAxis2Placement2D sweptRectPosition = sweptRectangle.getPosition();
          Axis3DNice rectangleAxisesNice = this.getAxis3DNice(sweptRectPosition) ;

          LIST<IfcLengthMeasure> c = sweptRectPosition.getLocation().getCoordinates();
          double xCenter = c.get(0).value;
          double yCenter = c.get(1).value;

          Vector3D centerPoint = new Vector3D(0, 0, 0);
          Rectangle3D shapeRoom = new Rectangle3D(centerPoint, xDim, yDim);

          rectanglePoints = shapeRoom.getListOfPoints();

          /*  Each point of the rectangle is expressed in the rectangle profile basis, so for each point
           *  we have to bring it in the coordinate space of the swept area  (rotation and displacement)
           *  and then to the coordinate space of the local placement  (again rotation and displacement).
           *  
           *  Rotation is provided by axes, while displacement is provided by the origin point
           *  that is the one obtained by getCoordinates() 
           *
           */
          for(int i = 0; i <rectanglePoints.size(); i++)
          {
            Vector3D point = rectanglePoints.remove(i);
            //point as is in the rectangle world

            Vector3D sweptAffine = rectangleAxisesNice.getDefaultWordCoordinate(point);
            //rotation: from rectangle profile axis into swept area axis - affine

            Vector3D  sweptOriginated = sweptAffine.getSumVector(new Vector3D(xCenter, yCenter, 0));
            //displacement: from swept area axis affine  to swept area axis


            Vector3D localAffine = axisesExtrudedAreaSolidNice.getDefaultWordCoordinate(sweptOriginated);
            //rotation :  from swept area into local placement axis - affine

            Vector3D localOriginated = pointOfAreaSolidNice.getSumVector(localAffine);
            //displacement: from local placement axis affine to local placement axis

            Vector3D globalAffine =
                localPlacementAxis.
                getDefaultWordCoordinate(localOriginated); 
            //rotation: from local placement axis  to global axis - affine

            Vector3D globalOriginated = globalAffine.getSumVector(localPlacementAbsoluteCoords);
            Vector3D absoluteCoordsRectPoint = new
                Vector3D(globalOriginated.first,
                    globalOriginated.second, 
                    globalOriginated.third);

            rectanglePoints.add(i, absoluteCoordsRectPoint);
          }

        }
      }
    }

    Rectangle3D rectAbsolutes = new Rectangle3D(rectanglePoints); 

    return rectAbsolutes;
  }


  /**
   * @param positionOfAreaSolid
   * @return
   */
  private Vector3D getPoint3DNice(IfcAxis2Placement3D positionOfAreaSolid) 
  {
    IfcCartesianPoint pointOfAreaSolid = positionOfAreaSolid.getLocation();
    return getPoint3DNice(pointOfAreaSolid);
  }



  private Vector3D getPoint3DNice(IfcCartesianPoint locationPoint) {
    if(locationPoint == null)
      return new Vector3D(0, 0, 0);
    double xOfPoint = locationPoint.getCoordinates().get(0).value;
    double yOfPoint = locationPoint.getCoordinates().get(1).value;
    double zOfPoint = locationPoint.getCoordinates().get(2).value;
    return new Vector3D(xOfPoint, yOfPoint, zOfPoint);
  }



  private IfcAxis2Placement3D getDefault3DAxis() {

    LIST<IfcLengthMeasure> mes = new LIST<IfcLengthMeasure>();
    IfcLengthMeasure mes1 = new IfcLengthMeasure(0);
    IfcLengthMeasure mes2 = new IfcLengthMeasure(0);
    IfcLengthMeasure mes3 = new IfcLengthMeasure(0);

    mes.add(mes1);
    mes.add(mes2);
    mes.add(mes3);



    IfcCartesianPoint location = new IfcCartesianPoint(mes);
    IfcAxis2Placement3D  axises = new IfcAxis2Placement3D(location, null, null);

    return axises;
  }

  private Axis3DNice getAxis3DNice(IfcAxis2Placement2D axis2d)
  {
    if(axis2d == null)
    {
      return this.getAxis3DNice(this.getDefault3DAxis());
    }

    IfcDirection xAxis  = axis2d.getRefDirection();
    double x1,x2,x3=0,  y1,y2,y3,  z1,z2,z3;
    if(xAxis == null )
    {
      x1 = 1;
      x2 = 0;
    }
    else
    {
      x1 = xAxis.getDirectionRatios().get(0).value;
      x2 = xAxis.getDirectionRatios().get(1).value;
    }

    z1 = 0;
    z2 = 0;
    z3 = 1;

    y1 = z3*x3 - z3*x2;
    y2 = z3*x1 - z1*x3;
    y3 = z1*x2 - z2*x1;

    return new Axis3DNice(x1, x2, x3, y1, y2, y3, z1, z2, z3);


  }

  

  private  Axis3DNice getAxis3DNice(IfcAxis2Placement3D axis3d)
  {
    if(axis3d == null)
    {
      return this.getAxis3DNice(this.getDefault3DAxis());
    }

    IfcDirection directionOfZAx = axis3d.getAxis();
    IfcDirection directionOfXAx = axis3d.getRefDirection();

    double x1,x2,x3,  y1,y2,y3,  z1,z2,z3;
    if(directionOfXAx == null)
    {
      x1 = 1;
      x2 = 0;
      x3 = 0;
    }
    else
    {
      LIST<DOUBLE> ratiosX = directionOfXAx.getDirectionRatios();
      x1 =  ratiosX.get(0).value;
      x2 = ratiosX.get(1).value;
      x3 =    ratiosX.get(2).value;
    }

    if(directionOfZAx == null)
    {
      z1 = 0;
      z2 = 0;
      z3 = 1;
    }
    else
    {
      LIST<DOUBLE> ratiosZ = directionOfZAx.getDirectionRatios();
      z1 =  ratiosZ.get(0).value;
      z2 = ratiosZ.get(1).value;
      z3 =    ratiosZ.get(2).value;
    }
    // Y = Z vectorial product X
    y1 = z3*x3 - z3*x2;
    y2 = z3*x1 - z1*x3;
    y3 = z1*x2 - z2*x1;

    return new Axis3DNice(x1, x2, x3, y1, y2, y3, z1, z2, z3);
  }


}