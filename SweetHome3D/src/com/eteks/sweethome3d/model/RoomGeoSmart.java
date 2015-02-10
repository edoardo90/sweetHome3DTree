package com.eteks.sweethome3d.model;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.parserobjects.ProfileShape3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Rectangle3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Segment3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Shape3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public class RoomGeoSmart extends Room {

  private static final long serialVersionUID = 2452728543226021893L;

  private Polygon polygon100Big = new Polygon();
  private ProfileShape3D polygon100BigShape3D ;
  
  /**
   * Points expressed in cm
   * @param points
   */
  public RoomGeoSmart(List<Vector3D> points)
  {
    super(points);
    for(Vector3D point : points)
    {
      this.addPointToPolygonAndToShape((float)point.first, 
          (float)point.second);
    }
  }

  /**
   * each point of the shape expressed in cm
   * @param shape3D
   */
  public RoomGeoSmart(Shape3D shape3D)
  {
    super(shape3D.getListOfPoints());
    for(Vector3D point : shape3D.getListOfPoints())
    {
      this.addPointToPolygonAndToShape((float)point.first, 
          (float)point.second);
    }

  }
  /**
   * <pre>
   * Area derived from a big polygon
   * a big polygon is one in which every coordinate is expressed in 
   * deci-millimiter
   * that means that for instance point A (2 meter, 3 meters) == A (200 cm, 300 cm) 
   * you make a polygon   with point A (20000, 30000) and then you call
   * {@link Area} myArea = new Area(myPolygon);
   * </pre>
   * @param alreadyBigArea
   */
  public RoomGeoSmart(Area alreadyBigArea)
  {
    this(areaToPointList(alreadyBigArea));
  }

  /**
   * <pre>
   * a big polygon is needed
   * a big polygon is one in which every coordinate is expressed in 
   * deci-millimiter
   * that means that for instance to make
   * point A (2 meter, 3 meters) == A (200 cm, 300 cm) 
   * you make a polygon   with point A (20000, 30000)
   * </pre>
   * @param alreadyBigArea
   */
  public RoomGeoSmart(Polygon polygonBig100)
  {
    this(polygonToList(polygonBig100));
  }
  
  /**
   * each point is expressed in cm
   * @param pointCenterOfSmallRoom
   */
  public RoomGeoSmart(Vector3D pointCenterOfSmallRoom)
  {
     super(getFloatArrayAroundPoint(pointCenterOfSmallRoom));
  }
  
  /**
   * a plain sweethome room
   * @param r
   */
  public RoomGeoSmart(Room r)
  {
    super( r.getPoints());
    this.setName(r.getName());
    this.addAllPoints(r.getPoints());
  }
  /**
   * <pre>
   * each point is expressed in cm
   * float [][]  points = new float[NUMBER_OF_POINTS][2];
   * for instance:
   * points[5][0] = x;
   * points[5][1] = y;
   * @param points
   */
  public RoomGeoSmart(float [][] points) {
    super(points);
    this.addAllPoints(points);
  }

  /**
   * point expressed in cm
   * @param x
   * @param y
   */
  public void addPointToPolygonAndToShape(float x, float y)
  {
    this.addPointToShape((int) (x * 100), (int)  (y * 100));

    Vector3D point = new Vector3D(x*100, y*100, 0);

    if(this.polygon100BigShape3D == null)
    {
      List<Vector3D> points = new ArrayList<Vector3D>();
      points.add(point);
      polygon100BigShape3D = new ProfileShape3D(points);

    }
    else
    {
      this.polygon100BigShape3D.addPoint(point);
    }
  }
  
  /**
   * The returned rectangle has coordinates expressed in cm
   * 
   * @return
   */
  public Rectangle3D getBoundingRoomRect3D()
  {
    Rectangle2D rect =  this.polygon100Big.getBounds2D();
    double xc = rect.getCenterX();
    double yc = rect.getCenterY();

    double height = rect.getHeight();
    double width =  rect.getWidth();

    Rectangle3D rect3D = new Rectangle3D(new Vector3D(xc/100, yc/100, 0), 
        width/100, height/100);
    return rect3D;
  }
  /**
   * return a room bigger, the new size is obtained adding borderSize to each point
   * in x and y towards the outside of the shape
   * @param borderSize expressed in cm
   * @return
   */
  public RoomGeoSmart getBiggerRoomBordered(float borderSize)
  {
    Rectangle2D rect =  this.polygon100Big.getBounds2D();
    double xc = rect.getCenterX()/100;
    double yc = rect.getCenterY()/100;

    float[][] points = this.getPoints();
    for(float[] row : points)
    {
      float xp = row[0];
      float yp = row[1];
      if(xp > xc)
        xp = xp + borderSize;
      else if(xp < xc)
        xp = xp - borderSize;

      if(yp > yc)
        yp = yp + borderSize;
      else if(yp < yc)
        yp = yp - borderSize;

      row[0] = xp;
      row[1] = yp;
    }


    RoomGeoSmart roomBigger = new RoomGeoSmart(points);
    return roomBigger;

  }



  /**
   * almost the same as getBiggerRoomBordered  but the points are multiplied
   * @param scaleFactor
   * @return
   */
  public RoomGeoSmart getBiggerCopyMultiplied(float scaleFactor)
  {

    float [][] points = this.getPoints();
    for(float [] row : points)
    {
      float x = row[0];
      float y = row[1];

      x = x*scaleFactor;
      y = y*scaleFactor;

      row[0] = x;
      row[1] = y;
    }

    Room biggerRoom = new Room(points);
    return new RoomGeoSmart(biggerRoom);

  }
  
  /**
   * 
   * @param r2
   * @return true iif there is an intersection with other room
   */
  public boolean intersect(RoomGeoSmart r2)
  {
    return this.intersect(r2, intersectionAlgorithm.AREA);
  }
  
  /**
   * <pre>
   * we consider intersection between room1 and wall,
   *             intersection between room2 and wall,
   *             we "draw" a segment linking the 2 centers
   *             we  look if the middle point falls inside the wall
   * @param r1
   * @param r2
   * @param wall
   * @return
   */
  public boolean isTheWallSeparating(RoomGeoSmart r2, RoomGeoSmart wall)
  {
    Area a1 = this.getAreaShape100Big();
    Area a2 = r2.getAreaShape100Big();
    float borderSize = (float) wall.getBoundingRoomRect3D().getMinEdge();
    wall = wall.getBiggerRoomBordered(borderSize);
    Area aw = wall.getAreaShape100Big();
    
    Area a1AndWall = (Area) a1.clone();
    a1AndWall.intersect(aw);
    
    Area a2AndWall = (Area) a2.clone();
    a2AndWall.intersect(aw);
    
    //now I have the "chunks" of rooms who are interested in the wall 
    //if we imagine the wall covered of fresh paint these (a2AndWall, a1AndWall) would be the dirty of paint areas
    
    RoomGeoSmart roomInters1 = new RoomGeoSmart(a1AndWall);
    RoomGeoSmart roomInters2 = new RoomGeoSmart(a2AndWall);
    
    Vector3D centroid1 = roomInters1.getCentroidRegular();
    Vector3D centroid2 = roomInters2.getCentroidRegular();
    
    Segment3D linkingCentroids = new Segment3D(centroid1, centroid2);
    Vector3D  midPointCC = linkingCentroids.getMidPoint();
    
    System.out.println(midPointCC);
    
    return wall.containsPoint(midPointCC);
  
  }
  
  /**
   * Point expressed in cm
   * @param point
   * @return
   */
  public boolean containsPoint(Vector3D point)
  {
    Area myArea = this.getAreaShape100Big();
    Vector3D pointBig = point.getScaledVector(100);
    
    return myArea.contains(pointBig.first, pointBig.second);
    
  }
  
  
  private Area getAreaShape100Big() {
    
    return new Area(this.polygon100Big);
  }

  /**
   * 
   * @param r2
   * @param algo
   * @return
   */
  private boolean intersect(RoomGeoSmart r2, intersectionAlgorithm algo)
  {

    Polygon p1 = this.polygon100Big;
    Polygon p2 = r2.polygon100Big;

    if(algo == null)
      algo = intersectionAlgorithm.AREA;

    if(algo ==  intersectionAlgorithm.AREA)
      return this.areaIntersect(p1, p2);
    else if(algo ==intersectionAlgorithm.INNER_POINTS)
      return this.pointsIntersect(p1, p2);

    return false;

  }

  private void addAllPoints(float [][] points)
  {
    for(float [] row : points)
    {
      float x = row[0];
      float y = row[1];
      this.addPointToPolygonAndToShape(x, y);
    }
  }

  
  private void addPointToShape(int x, int y)
  {
    this.polygon100Big.addPoint(x , y );

  }
  /**
   * true iif intersection exists
   * @param p1
   * @param p2
   * @return
   */
  private boolean areaIntersect(Polygon p1, Polygon p2)
  {
    Area a1 = new Area(p1);
    Area a2 = new Area(p2);

    Area intersectionArea = (Area)a1.clone();

    intersectionArea.intersect(a2);
    return ! intersectionArea.isEmpty();

  }
  
  
  

  private boolean pointsIntersect(Polygon p1, Polygon p2)
  {
    Point p;
    for(int i = 0; i < p2.npoints;i++)
    {
      p = new Point(p2.xpoints[i],p2.ypoints[i]);
      if(p1.contains(p))
        return true;
    }
    for(int i = 0; i < p1.npoints;i++)
    {
      p = new Point(p1.xpoints[i],p1.ypoints[i]);
      if(p2.contains(p))
        return true;
    }
    return false;
  }

  @Override
  /**
   * representation of points in cm
   */
  public String toString()
  {

    String s = "";
    float [][] points = this.getPoints();
    for(int i=0;i<points.length; i++)
    {
      s = s + "(" + points[i][0] + ", " + points[i][1] + ")"   + "\n";
    }
    return s;

  }

  /**
   * centroid (x, y)  where x = avg (Xi)  of the shape, same with y
   * @return  centroid expressed in deci-millimiter
   */
  public Vector3D getCentroid100Big() 
  {
    double xTot=0, yTot =0;
    List<Vector3D> points = this.polygon100BigShape3D.getListOfPoints();
    for(Vector3D point : points)
    {
      xTot+= point.first;
      yTot+=point.second;
    }
    xTot = xTot / points.size();
    yTot = yTot / points.size();
    return new Vector3D(xTot, yTot, 0);
  }
  /**
   * centroid (x, y)  where x = avg (Xi)  of the shape, same with y
   * @return centroid expressed in centimeter
   */
  public Vector3D getCentroidRegular()
  {
    return this.getCentroid100Big().getScaledVector(0.01f);
  }



  private enum intersectionAlgorithm
  {
    INNER_POINTS, AREA
  }


  private static List<Vector3D>  polygonToList(Polygon polygon)
  {
    int index = 0;
    PathIterator clockIter = polygon.getPathIterator(null);
    List<Vector3D> points = new ArrayList<Vector3D>();
    while(!clockIter.isDone() && index < polygon.npoints) {
      float[] coords = new float[2];
      clockIter.currentSegment(coords);
      double x = coords[0];
      double y = coords[1];
      Vector3D point = new Vector3D(x, y, 0);
      points.add(point);
      clockIter.next();
      index++;
    }
    return points;
  }

  private static  List<Vector3D> areaToPointList(Area alreadyBigArea) {
    PathIterator pi = alreadyBigArea.getPathIterator(null);
    List<Vector3D> points = new ArrayList<Vector3D>();
    while (pi.isDone() == false) {
      Vector3D point = describeCurrentSegment(pi);
      if(point != null)
      {
        points.add(point);
        point.scale(0.01f);
      }
      pi.next();
    }

    Vector3D head = points.remove(0);

    Collections.reverse(points);

    points.add(0, head);

    return points;
  }

  private  static Vector3D describeCurrentSegment(PathIterator pi) {
    double[] coordinates = new double[6];
    double x, y;
    int type = pi.currentSegment(coordinates);
    switch (type) {
      case PathIterator.SEG_MOVETO:

        x = coordinates[0];
        y = coordinates[1];
        return new Vector3D(x, y, 0);

      case PathIterator.SEG_LINETO:

        x = coordinates[0];
        y = coordinates[1];
        return new Vector3D(x, y, 0);
      case PathIterator.SEG_QUADTO:

        break;
      case PathIterator.SEG_CUBICTO:

        break;
      case PathIterator.SEG_CLOSE:
        break;
      default:
        break;
    }
    return null;
  }

  public RoomGeoSmart intersectionAreaRoom(RoomGeoSmart room2) {

    Polygon p1 = this.polygon100Big;
    Polygon p2 = room2.polygon100Big; 

    Area a1 = new Area(p1);
    Area a2 = new Area(p2); 

    a1.intersect(a2);

    RoomGeoSmart intersAreaRoom = new RoomGeoSmart(a1);

    return intersAreaRoom;
  }
  
  
  
  
  private static float [][] getFloatArrayAroundPoint(Vector3D pointCenterOfSmallRoom) {
    
    float xc = (float)pointCenterOfSmallRoom.first;
    float yc = (float)pointCenterOfSmallRoom.second;
    
    float x1 = xc - 30;
    float y1 = yc - 30;
    
    float x2 = xc + 30;
    float y2 = yc - 30;

    float x3 = xc + 30;
    float y3 = yc + 30;
    
    float x4 = xc - 30;
    float y4 = yc + 30;
    
    
    return new float [][]{{x1, y1}, {x2, y2}, {x3, y3}, {x4, y4}};
  }
  
  
  
}






