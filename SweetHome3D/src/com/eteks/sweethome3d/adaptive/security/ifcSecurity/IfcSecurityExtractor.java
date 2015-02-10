package com.eteks.sweethome3d.adaptive.security.ifcSecurity;

import ifc2x3javatoolbox.ifc2x3tc1.DOUBLE;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement2D;
import ifc2x3javatoolbox.ifc2x3tc1.IfcAxis2Placement3D;
import ifc2x3javatoolbox.ifc2x3tc1.IfcCartesianPoint;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDirection;
import ifc2x3javatoolbox.ifc2x3tc1.IfcDoor;
import ifc2x3javatoolbox.ifc2x3tc1.IfcElement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcExtrudedAreaSolid;
import ifc2x3javatoolbox.ifc2x3tc1.IfcFeatureElementSubtraction;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLengthMeasure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcLocalPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcObjectDefinition;
import ifc2x3javatoolbox.ifc2x3tc1.IfcObjectPlacement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcOpeningElement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProduct;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProductDefinitionShape;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProductRepresentation;
import ifc2x3javatoolbox.ifc2x3tc1.IfcProfileDef;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRectangleProfileDef;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelContainedInSpatialStructure;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelDecomposes;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelFillsElement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelSpaceBoundary;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRelVoidsElement;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRepresentation;
import ifc2x3javatoolbox.ifc2x3tc1.IfcRepresentationItem;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSIPrefix;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSIUnit;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSIUnitName;
import ifc2x3javatoolbox.ifc2x3tc1.IfcSpace;
import ifc2x3javatoolbox.ifc2x3tc1.IfcWall;
import ifc2x3javatoolbox.ifc2x3tc1.LIST;
import ifc2x3javatoolbox.ifc2x3tc1.SET;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

import java.awt.geom.Area;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildinLinkWallWithDoor;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingLinkEdge;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingLinkWall;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingRoomNode;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.DoorObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.UnknownObject;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Axis3DNice;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Placement3DNice;
import com.eteks.sweethome3d.adaptive.security.parserobjects.ProfileShape3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Rectangle3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Segment3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Shape3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.RoomGeoSmart;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.model.Wall;

public class IfcSecurityExtractor {

  private String ifcFileName;
  private IfcModel ifcModel;
  private List<IfcSpace> ifcSpaces = new ArrayList<IfcSpace>();
  private List<String>   addedWalls = new ArrayList<String>();

  private Map<IfcSpace, BuildingRoomNode> spaceToRoomNoode  = new HashMap<IfcSpace, BuildingRoomNode>();
  protected ConfigLoader configLoader;

  public IfcSecurityExtractor(String ifcFileName, UserPreferences preferences)
  {
    this.ifcFileName = ifcFileName;

    this.configLoader = ConfigLoader.getInstance(preferences); 

    this.setMapOfLibraryObjects(preferences);

  }

  protected void setMapOfLibraryObjects(UserPreferences preferences)
  {
    Map<BuildingObjectType, HomePieceOfFurniture> map =   configLoader.createFurnitureMap();

    preferences.setFornitureMap(map); 
  }


  public BuildingSecurityGraph getGraphFromFile() throws Exception
  {
    BuildingSecurityGraph buildingSecurityGraph = new BuildingSecurityGraph();

    List<BuildingLinkEdge> linkEdgeList;
    List<BuildingRoomNode> roomNodeList;
    List<Wall>  notLinkingWalls;

    ifcModel = new IfcModel();

    File stepFile = new File(this.ifcFileName);

    ifcModel.readStepFile(stepFile);

    Collection<IfcSpace> ifcSpacesColl = ifcModel.getCollection(IfcSpace.class);

    if(ifcSpacesColl.size() == 0)
      throw new IllegalStateException("Please use your Bim software to tag rooms");

    this.ifcSpaces.addAll(ifcSpacesColl);

    float scaleFactor ;
    scaleFactor = this.getScaleFactor();

    roomNodeList = this.getRooms(scaleFactor);
    linkEdgeList = this.getLinks(scaleFactor);
    notLinkingWalls = this.getAllOtherWalls(scaleFactor);

    buildingSecurityGraph.setLinkEdgeList(linkEdgeList);
    buildingSecurityGraph.setRoomNodeList(roomNodeList);
    buildingSecurityGraph.setNotLinkingWalls(notLinkingWalls);

    return buildingSecurityGraph;
  }

  private List<Wall> getAllOtherWalls(float scaleFactor) {
    List<Wall> walls = new ArrayList<Wall>();
    Collection<IfcWall> ifcWallsColl = ifcModel.getCollection(IfcWall.class);

    for(IfcWall ifcWall : ifcWallsColl)
    {
      String wallId =  ifcWall.getGlobalId().getDecodedValue();

      if(! this.addedWalls.contains(wallId))
      {
        Shape3D wallShape = getShapeAndPosition(ifcWall, scaleFactor);
        RoomGeoSmart smartWallSeenAsRoom = new RoomGeoSmart(wallShape); //puts points in shape3d and polygon
        Rectangle3D rectWall = smartWallSeenAsRoom.getBoundingRoomRect3D();
        Wall wall = rectWall.getWall();
        walls.add(wall);
      }
    }
    return walls;
  }

  /**
   * <pre>
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
   * </pre>
   * @return
   */
  protected float getScaleFactor()
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

      if(pr.equals("MILLI") &&  un.equals("METRE"))
      {
        scaleF = 0.10f;
      }


    }
    return scaleF;
  }


  private List<BuildingLinkEdge> getLinks(float scaleFactor)
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

            IfcWall boundingWall = null;
            Shape3D wallShape = null;
            if(relatingSpace != spaceToTest && elementBounding instanceof IfcWall)
            {
              String idRoom1 = relatingSpace.getGlobalId().getDecodedValue();
              String idRoom2 = spaceToTest.getGlobalId().getDecodedValue();

              boundingWall = (IfcWall) elementBounding;
              wallShape = getShapeAndPosition(boundingWall, scaleFactor);
              String wallID = boundingWall.getGlobalId().getDecodedValue();


              if(areIntersected(relatingSpace, spaceToTest, wallShape))
              {

                String longNameFirs = spaceToTest.getLongName().getDecodedValue();
                String longNameSecond = relatingSpace.getLongName().getDecodedValue();

                RoomGeoSmart smartWallSeenAsRoom = new RoomGeoSmart(wallShape);
                Rectangle3D rectWall = smartWallSeenAsRoom.getBoundingRoomRect3D();

                float angleOfWall = rectWall.getAngleOfLongsEdges(); //For Door! not for wall
                //because angle of wall in already "included" in its shape

                Wall wall = rectWall.getWall();
                if(!this.addedWalls.contains(wallID))
                  this.addedWalls.add(wallID);

                List<DoorObject> doors = this.getDoors(elementBounding, scaleFactor);
                //door id is setted here for every door


                BuildingLinkEdge link = null;
                boolean thereIsADoor = false;

                for(DoorObject doorObj : doors)
                {
                  if(areTwoRoomsConnectedByDoor(doorObj,  idRoom1, idRoom2 ) )
                  { 
                    doorObj.setAngle(angleOfWall);
                    link =
                        new BuildinLinkWallWithDoor(wall, doorObj, longNameFirs, longNameSecond);

                    thereIsADoor = true;
                  }
                }

                //there is no door in wall connecting the two rooms we are considerating here
                if(!thereIsADoor)
                {
                  link =        new BuildingLinkWall(wall,  longNameFirs, longNameSecond);
                  link.setId(wallID);
                }


                if(!  buildingLinkEdgeList.contains(link))
                { 
                  buildingLinkEdgeList.add(link);
                }

              }
            }

          }
        }
      }
    }

    Collections.sort(buildingLinkEdgeList, new Comparator<BuildingLinkEdge>() {

      public int compare(BuildingLinkEdge o1, BuildingLinkEdge o2) {
        // TODO Auto-generated method stub
        return (""+o1).compareTo(""+o2);
      }
    });
    System.out.println(buildingLinkEdgeList);

    return buildingLinkEdgeList;

  }


  private boolean areTwoRoomsConnectedByDoor(DoorObject door, String idRoom1, String idRoom2) {
    
    return (  (door.getIdRoom1().equals(idRoom1) && door.getIdRoom2().equals(idRoom2))   ||
              (door.getIdRoom2().equals(idRoom1) && door.getIdRoom1().equals(idRoom2)));
    
  }

  private List<DoorObject> getDoors(IfcElement elementBounding, float scaleFactor) {

    List<DoorObject> doors = new ArrayList<DoorObject>();

    SET<IfcRelVoidsElement> openings = elementBounding.getHasOpenings_Inverse();
    DoorObject door = new DoorObject();
    if(openings != null)
    {
      Iterator<IfcRelVoidsElement> iterOp = openings.iterator();
      IfcRelVoidsElement voidd = iterOp.next();  
      IfcFeatureElementSubtraction opening = voidd.getRelatedOpeningElement();

      String id1 = "";
      String id2 = "";
      
      if (opening instanceof IfcOpeningElement) {
        IfcOpeningElement openElem = (IfcOpeningElement)opening;
        SET<IfcRelFillsElement> fillings = openElem.getHasFillings_Inverse();
        IfcRelFillsElement fill = fillings.iterator().next();
        IfcElement doorElem = fill.getRelatedBuildingElement();
        if (doorElem instanceof IfcDoor) {
          IfcDoor ifcDoor = (IfcDoor)doorElem;
          Iterator<IfcRelSpaceBoundary> spacesIter = ifcDoor.getProvidesBoundaries_Inverse().iterator();
          IfcSpace sp1 = spacesIter.next().getRelatingSpace();
          IfcSpace sp2 = spacesIter.next().getRelatingSpace();
          id1 = sp1.getGlobalId().getDecodedValue();
          id2 = sp2.getGlobalId().getDecodedValue();
        }
        
      }
      
      
      door.setIdRoom1(id1);
      door.setIdRoom2(id2);

      Vector3D position = getPositionOfProduct(opening);
      String id = voidd.getGlobalId().getDecodedValue();
      door.setId(id);
      position.scale(scaleFactor);
      door.setPosition(position);
      doors.add(door);
    }

    return doors;

    //TODO  shape.. ?? maybe next time?

  }

  private boolean areIntersected(IfcSpace space1, IfcSpace space2, Shape3D wallShape) throws IllegalStateException {

    BuildingRoomNode brn = this.spaceToRoomNoode.get(space1);
    if(brn == null)
    {
      throw new IllegalStateException("the map spaces to BuildingRoomNode is not well filled");
    }
    RoomGeoSmart smart1 = brn.getRoomSmart();


    BuildingRoomNode brnSpace2 = this.spaceToRoomNoode.get(space2);
    if(brnSpace2 == null)
    {
      throw new IllegalStateException("the map spaces to BuildingRoomNode is not well filled");
    }
    RoomGeoSmart smart2 = brnSpace2.getRoomSmart();

    RoomGeoSmart smartWallSeenAsRoom = new RoomGeoSmart(wallShape);

    Rectangle3D bb = smartWallSeenAsRoom.getBoundingRoomRect3D();
    float borderSize = (float)bb.getMinEdge();

    RoomGeoSmart smart1Bigger = smart1.getBiggerRoomBordered(borderSize);
    RoomGeoSmart smart2Bigger = smart2.getBiggerRoomBordered(borderSize);

    //intersection between 2 rooms
    boolean instersect = smart1Bigger.intersect(smart2Bigger);

    smartWallSeenAsRoom.getBiggerRoomBordered(borderSize*2);
    //TODO: debug  --- boolean isWallSeparating = isTheWallSeparating(smart1, smart2, smartWallSeenAsRoom);


    return instersect ;

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

        //shape and position
        Shape3D roomShape = getShapeAndPosition(space, scaleFactor); 


        //containement
        List<BuildingObjectContained> objects = getObjectsOfRoom(space, scaleFactor);

        BuildingRoomNode buildingRoomNode = new
            BuildingRoomNode(roomName, roomShape, objects);
        buildingRoomNode.setId(idRoom);

        buildingRoomList.add(buildingRoomNode);

        this.spaceToRoomNoode.put(space, buildingRoomNode);
      }

    }


    return buildingRoomList;

  }

  private String getStoreyName(IfcSpace firstRoom) {

    SET<IfcRelDecomposes> decInv = firstRoom.getDecomposes_Inverse();
    Iterator<IfcRelDecomposes> decInvIter = decInv.iterator();
    IfcRelDecomposes relDecomp = decInvIter.next();

    IfcObjectDefinition rob = relDecomp.getRelatingObject();
    return rob.getGlobalId().getDecodedValue();

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
          singleFurniture.setId(furnitureProduct.getGlobalId().getDecodedValue());

          if(! (singleFurniture instanceof UnknownObject))
          {
            contained.add(singleFurniture);
          }


          String productName = furnitureProduct.getName().getDecodedValue();

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

      List<String> toLookStrings = this.configLoader.stringToLookFor(objType);
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



  private Shape3D getShapeAndPosition(IfcProduct product, float scaleFactor) 
  {

    if(product == null)
      throw new IllegalStateException("passed product is null!");

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


  private Shape3D getShape(IfcProduct product, Axis3DNice localPlacementAxis, Vector3D localPlacementAbsoluteCoords) 
  {
    IfcProductRepresentation representation = product.getRepresentation();
    List<Vector3D> extrudedAreaPoints = new ArrayList<Vector3D>();

    if(representation instanceof IfcProductDefinitionShape)
    {
      IfcProductDefinitionShape definitionShape = (IfcProductDefinitionShape) representation;
      LIST<IfcRepresentation> representationList = definitionShape.getRepresentations();
      Iterator<IfcRepresentation> representationsIterator = representationList.iterator();
      while(representationsIterator.hasNext() && extrudedAreaPoints.isEmpty())
      {  
        IfcRepresentation shapeRepr = representationsIterator.next();
        extrudedAreaPoints = this.getextrudedAreaPoints(shapeRepr, extrudedAreaPoints,  localPlacementAxis,  localPlacementAbsoluteCoords);
      }
    }

    Rectangle3D rectAbsolutes= null;
    try
    {
      rectAbsolutes = new Rectangle3D(extrudedAreaPoints); 
    }
    catch(IllegalStateException e)
    {
      rectAbsolutes = null;
    }
    ProfileShape3D shapeAbsolutes = null;
    if(rectAbsolutes != null)
    {
      return rectAbsolutes;
    }
    else
    {
      shapeAbsolutes = new ProfileShape3D(extrudedAreaPoints);
      return shapeAbsolutes;
    }
  }

  private List<Vector3D> getextrudedAreaPoints(IfcRepresentation shapeRepr, List<Vector3D> extrudedAreaPoints,
      Axis3DNice localPlacementAxis, Vector3D localPlacementAbsoluteCoords)
      {
    SET<IfcRepresentationItem> items = shapeRepr.getItems();
    Iterator<IfcRepresentationItem> iter = items.iterator();
    IfcRepresentationItem representItem = iter.next() ;
    if(representItem instanceof IfcExtrudedAreaSolid)
    {
      extrudedAreaPoints = 
          getPointsFromExtrudedArea(extrudedAreaPoints, representItem, localPlacementAxis, localPlacementAbsoluteCoords);
    }

    return extrudedAreaPoints;
      }

  private List<Vector3D>   getPointsFromExtrudedArea(List<Vector3D> extrudedAreaPoints, IfcRepresentationItem representItem,
      Axis3DNice localPlacementAxis, Vector3D localPlacementAbsoluteCoords)
      {
    //area solid  contains  swept area

    IfcExtrudedAreaSolid areaSolid = (IfcExtrudedAreaSolid) representItem; 
    IfcAxis2Placement3D axisesAndPositionOfExtrudedAreaSolid = areaSolid.getPosition();

    Axis3DNice axisesExtrudedAreaSolidNice = getAxis3DNice(axisesAndPositionOfExtrudedAreaSolid);
    Vector3D pointOfAreaSolidNice = getPoint3DNice( axisesAndPositionOfExtrudedAreaSolid );


    IfcProfileDef sweptArea = areaSolid.getSweptArea();

    if(sweptArea instanceof IfcRectangleProfileDef)
    {
      extrudedAreaPoints = getPointsOfRectangularProfile(extrudedAreaPoints, 
          axisesExtrudedAreaSolidNice, pointOfAreaSolidNice, 
          sweptArea,
          localPlacementAbsoluteCoords,
          localPlacementAxis
          );
    }

    return extrudedAreaPoints;

      }


  private List<Vector3D> getPointsOfRectangularProfile( List<Vector3D> extrudedAreaPoints, Axis3DNice axisesExtrudedAreaSolidNice , 
      Vector3D pointOfAreaSolidNice, IfcProfileDef sweptArea, Vector3D localPlacementAbsoluteCoords, Axis3DNice localPlacementAxis )
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

    extrudedAreaPoints = shapeRoom.getListOfPoints();

    /*  Each point of the rectangle is expressed in the rectangle profile basis, so for each point
     *  we have to bring it in the coordinate space of the swept area  (rotation and displacement)
     *  and then to the coordinate space of the local placement  (again rotation and displacement).
     *  
     *  Rotation is provided by axes, while displacement is provided by the origin point
     *  that is the one obtained by getCoordinates() 
     *
     */
    for(int i = 0; i <extrudedAreaPoints.size(); i++)
    {
      Vector3D point = extrudedAreaPoints.remove(i);
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

      extrudedAreaPoints.add(i, absoluteCoordsRectPoint);
    }

    return extrudedAreaPoints;

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