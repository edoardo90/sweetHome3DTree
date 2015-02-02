package com.eteks.sweethome3d.tools.security.ifcSecurity;

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

import Jama.Matrix;

import com.eteks.sweethome3d.tools.security.buildingGraph.BuildingLinkEdge;
import com.eteks.sweethome3d.tools.security.buildingGraph.BuildingRoomNode;
import com.eteks.sweethome3d.tools.security.buildingGraphObjects.BuildingSecurityGraph;

public class IfcSecurityExtractor {

  private String ifcFileName;
  private IfcModel ifcModel;
  private List<IfcSpace> ifcSpaces = new ArrayList<IfcSpace>();

  public IfcSecurityExtractor(String ifcFileName)
  {
    this.ifcFileName = ifcFileName;
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


    linkEdgeList = this.getLinks();
    roomNodeList = this.getRooms();

    buildingSecurityGraph.setLinkEdgeList(linkEdgeList);
    buildingSecurityGraph.setRoomNodeList(roomNodeList);

    return buildingSecurityGraph;
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
                BuildingLinkEdge(relatingSpace.getGlobalId().getDecodedValue(),
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

    List<BuildingRoomNode> buildingRoomList = new ArrayList<BuildingRoomNode>();

    IfcSpace firstRoom = ifcSpaces.get(0);
    String storeyName =  getStoreyName(firstRoom);

    for(IfcSpace space : ifcSpaces)
    {
      if(getStoreyName(space).equals(storeyName))
      {
        String roomName = space.getLongName().getDecodedValue();
        System.out.println("\n _________________________________\n storey : "
        + storeyName + "\n room: " + roomName 
        + " room ID : "  + space.getGlobalId().getDecodedValue());
        
        //shape and position
        Rectangle3D roomShape = getShapeAndPosition(space);
        System.out.println("room shape: \n" + roomShape);

        //containement
        List<Object> objects = getObjectsOfRoom(space);
        System.out.println( "contains: " +  objects);


        BuildingRoomNode buildingRoomNode = new BuildingRoomNode(roomShape, objects);
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


  private List<Object> getObjectsOfRoom(IfcSpace space) {

    List<Object> conteined = new ArrayList<Object>();
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
          IfcProduct product = iteratorProductContained.next();
          conteined.add(product);
          String productName = product.getName().getDecodedValue();
          System.out.println("\t\t " + productName);
        }
      }
    }

    return conteined;
  }


  private Rectangle3D getShapeAndPosition(IfcProduct product) 
  {

    Placement3DNice placementNice = getPlacementFromObject(product);
    Axis3DNice axis3DContainer = placementNice.getAxes();
    Vector3D   absoluteCoordinates = placementNice.getOriginPoint();
    
        
    Rectangle3D shapeLocated = this.getShape(product, axis3DContainer, absoluteCoordinates);
    return shapeLocated;
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
    List<Vector3D> rectanglePoints = new ArrayList<IfcSecurityExtractor.Vector3D>();

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


  public class Rectangle3D
  {

    private Vector3D  pointNorthWest, pointSouthEast;

    public Vector3D getPointNorthWest()
    {
      double x = this.pointNorthWest.first;
      double y = this.pointNorthWest.second;
      double z = this.pointNorthWest.third;
      return new Vector3D(x,y,z);
    }


    public void multiplyEdgesCoordsBy(int i) {
      this.pointNorthWest.first *= i;
      this.pointNorthWest.second *= i;
      this.pointNorthWest.third *= i;

      this.pointSouthEast.first *= i;
      this.pointSouthEast.second *= i;
      this.pointSouthEast.third *= i;

    }


    public double getArea() 
    {
      double xDim = Math.abs( this.pointNorthWest.first - this.pointSouthEast.first );
      double yDim = Math.abs( this.pointNorthWest.second - this.pointSouthEast.second);
      return xDim * yDim;
    }

    public double getHeight()
    {
      return  Math.abs( this.pointNorthWest.second - this.pointSouthEast.second);
    }

    public double getWidth() 
    {
      return Math.abs( this.pointNorthWest.first - this.pointSouthEast.first ); 
    }

    public double getMinEdge() { return min(getHeight(), getWidth()); }

    private double min(double x, double y)  {     return x<y? x : y;   }

    public Vector3D getPointSouthEast()
    {
      double x = this.pointSouthEast.first;
      double y = this.pointSouthEast.second;
      double z = this.pointSouthEast.third;
      return new Vector3D(x,y,z);
    }

    @Override
    public String toString()
    {
      String p1 = this.getPointNorthWest().toStringShortXY();
      String p2 = this.getPointNorthEast().toStringShortXY();
      String p3 = this.getPointSouthWest().toStringShortXY();
      String p4 = this.getPointSouthEast().toStringShortXY();

      return  p1 + " ----------------------- " + p2  +
          "\n | " + "                             | " +  
          "\n | " + "                             | " +
          "\n | " + "                             | " +
          "\n +"  + p3 +  "--------------"  + p4 + "\n"; 
    }


    /* 
     *  <---x_DIM---------->   
     *  1------------------2    ^
     *  |                  |    |            ^
     *  |         O        |  Y_DIM          |
     *  |                  |    |            y
     *  4------------------3    v            |---x-->
     */
    public Vector3D getPointNorthEast()
    {

      double x1, y1, x2, x3, y2, y3, x4, y4;

      x1 = this.pointNorthWest.first;
      y1 = this.pointNorthWest.second;

      x3 = this.pointSouthEast.first;
      y3 = this.pointSouthEast.second;

      x2 = x3;
      y2 = y1;

      x4= x1;
      y4 = y3;

      return new Vector3D(x2, y2, this.pointNorthWest.third);

    }

    public Vector3D getPointSouthWest()
    {

      double x1, y1, x2, x3, y2, y3, x4, y4;

      x1 = this.pointNorthWest.first;
      y1 = this.pointNorthWest.second;

      x3 = this.pointSouthEast.first;
      y3 = this.pointSouthEast.second;

      x2 = x3;
      y2 = y1;

      x4= x1;
      y4 = y3;

      return new Vector3D(x4, y4, this.pointNorthWest.third);

    }

    public List<Vector3D> getListOfPoints()
    {
      Vector3D p1 = this.getPointNorthEast();
      Vector3D p2 = this.getPointNorthWest();
      Vector3D p3 = this.getPointSouthWest();
      Vector3D p4 = this.getPointSouthEast();
      List<Vector3D> lst = new ArrayList<Vector3D>();

      lst.add(p1);
      lst.add(p2);
      lst.add(p3);
      lst.add(p4);

      return lst;
    }


    /* 
     *  <---x_DIM---------->   
     *  1------------------2    ^
     *  |                  |    |            ^
     *  |         O        |  Y_DIM          |
     *  |                  |    |            y
     *  4------------------3    v            |---x-->
     */

    public Rectangle3D(Vector3D center, double xDim, double yDim)
    {
      double x1, y1, z1 = center.third,   x3,y3,z3 = center.third;

      x1 = center.first - xDim / 2;
      y1 = center.second + yDim / 2;
      x3 = center.first + xDim / 2;
      y3 = center.second - yDim / 2;

      this.pointNorthWest = new Vector3D(x1, y1, z1);
      this.pointSouthEast = new Vector3D(x3, y3, z3);

    }


    public Rectangle3D(List<Vector3D> list)
    {
      if(list == null || list.size() != 4)
      {
        this.pointNorthWest = new Vector3D();
        this.pointSouthEast = new Vector3D(1,1,0);
      }
      else
      {

        /*
         * NW    N    NE
         *    
         * W     +     E
         *     
         *SW     S    SE
         * 
         */

        double minX = 0, maxY=0;
        minX = list.get(0).first;
        maxY = list.get(0).second;
        for(int i=0;i<4;i++)
        { 
          double x = list.get(i).first;
          double y = list.get(i).second;
          if(x <= minX)
            minX = x;
          if(y >= maxY)
            maxY = y;
        }

        for(int i=0;i<4;i++)
        {
          double x = list.get(i).first;
          double y = list.get(i).second;

          if(x==minX && y == maxY)
          {
            this.pointNorthWest = list.get(i);
          }
          if(x!=minX && y!= maxY)
          {
            this.pointSouthEast = list.get(i);
          }

        }

      }
    }






  }

  private class Placement3DNice
  {
    private Axis3DNice axes;
    private Vector3D originPoint;
    
    public Placement3DNice(Axis3DNice axes, Vector3D originPoint)
    {
      this.axes = axes;
      this.originPoint = originPoint;
    }
    
    public Axis3DNice getAxes() {
      return axes;
    }
    public void setAxes(Axis3DNice axes) {
      this.axes = axes;
    }
    public Vector3D getOriginPoint() {
      return originPoint;
    }
    public void setOriginPoint(Vector3D originPoint) {
      this.originPoint = originPoint;
    }
  }

  private class Axis3DNice
  {

    public Vector3D xVector = this.getDefaultVector(1),
        yVector = this.getDefaultVector(2),
        zVector = this.getDefaultVector(3);

    @Override
    public String toString()
    {
      return "X: " + this.xVector + "\n" +
          "Y: " + this.yVector + "\n" +
          "Z: " + this.zVector ;
    }

    public Axis3DNice()
    {
      this(1,0,0,   0,1,0,   0,0,1);
    }

    public Axis3DNice(double x1, double x2,double x3,double y1,double y2,double y3,double z1,double z2,double z3)
    {
      this.xVector.first = x1;
      this.xVector.second = x2;
      this.xVector.third = x3;

      this.yVector.first = y1;
      this.yVector.second = y2;
      this.yVector.third = y3;


      this.zVector.first = z1;
      this.zVector.second = z2;
      this.zVector.third = z3;
    }

    /** 
     * Converts coordinates of point express in the basis of this Axis3D into canonical coordinates
     * 
     * Let V a vector in a space in the form [v1, v2, v3],   and let A [Ax, Ay, Az] a basis of that space
     * then V expressed in canonical form is obtained as V_canonical = A * V
     * 
     * @return the coordinate respect to the canonical  standard word (xyz)=(1,0,0  0,1,0   0,0,1)
     */
    public Vector3D getDefaultWordCoordinate(Vector3D coordinate)
    {
      double v1,v2, v3;
      v1 = coordinate.first;
      v2 = coordinate.second;
      v3 = coordinate.third;


      double[][] array = this.getMatrix();
      Matrix A = new Matrix(array);   //basis matrix of this 3d axes
      Matrix v = new Matrix(new double[]{v1, v2, v3}, 1); //row vector     

      Matrix vCanonical = A.times(v.transpose());

      double first = vCanonical.get(0, 0);
      double second = vCanonical.get(1, 0);
      double third = vCanonical.get(2, 0);






      Vector3D vCanonicalNice = new Vector3D(first, second, third);

      return vCanonicalNice;
    }

    private double[][] getMatrix()
    {
      double x1,x2,x3,y1,y2,y3,z1,z2,z3;

      x1 = this.xVector.first;
      x2 = this.xVector.second;
      x3 = this.xVector.third;

      y1 = this.yVector.first;
      y2 = this.yVector.second;
      y3 = this.yVector.third;


      z1 = this.zVector.first;
      z2 = this.zVector.second;
      z3 = this.zVector.third;


      double [][] array = {{x1,y1,z1}, {x2,y2,z2}, {x3,y3,z3}};

      return array;
    }

    private double scalarProduct(Vector3D v1, Vector3D v2)
    {
      return v1.first * v2.first + v1.second * v2.second + v1.third * v2.third;
    }

    private Vector3D getDefaultVector(int i)
    {
      Vector3D vec = new Vector3D(0, 0, 0);
      switch (i) {
        case 1:
          vec.first = 1;
          break;
        case 2:
          vec.second = 1;
          break;
        case 3:
          vec.third = 1;
          break;
        default:
          return null;

      }
      return vec;

    }
  }


  public class Vector3D
  {
    public double first;
    public double second;
    public double third;

    public Vector3D(double first, double second, double third)
    {
      if(Math.abs(first)< 0.001)
        first =0;
      if(Math.abs(second)< 0.001)
        second =0;
      if(Math.abs(third)< 0.001)
        third =0;



      this.first = first;
      this.second = second;
      this.third = third;
    }

    public Vector3D getSumVector(Vector3D vector3d) {
      return new Vector3D(this.first + vector3d.first ,
          this.second + vector3d.second,
          this.third  + vector3d.third);
    }

    public Vector3D()
    {
      this(0,0,0);
    }

    public String toStringShortXY()
    {
      return "("+ round(this.first,3) + ", " +  round(this.second,3) + ")";
    }

    private double round(double value, int places) {
      if (places < 0) throw new IllegalArgumentException();

      long factor = (long) Math.pow(10, places);
      value = value * factor;
      long tmp = Math.round(value);
      return (double) tmp / factor;
    }

    public void sumVector(Vector3D vec)
    {
      this.first += vec.first;
      this.second += vec.second;
      this.third += vec.third;

    }

    @Override
    public String toString()
    {
      return "[ " +  this.first + ", " + this.second + ", " + this.third  + " ]";
    }
  }

}