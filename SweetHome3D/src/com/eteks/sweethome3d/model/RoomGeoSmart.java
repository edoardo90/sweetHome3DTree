package com.eteks.sweethome3d.model;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.parserobjects.ProfileShape3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Rectangle3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Shape3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public class RoomGeoSmart extends Room {

  private static final long serialVersionUID = 2452728543226021893L;

  private Polygon polygon100Big = new Polygon();
  private ProfileShape3D polygon100BigShape3D ;

  public RoomGeoSmart(List<Vector3D> points)
  {
    super(points);
    for(Vector3D point : points)
    {
      this.addPointToPolygonAndToShape((float)point.first, 
          (float)point.second);
    }
  }

  public RoomGeoSmart(Shape3D shape3D)
  {
    super(shape3D.getListOfPoints());
    for(Vector3D point : shape3D.getListOfPoints())
    {
      this.addPointToPolygonAndToShape((float)point.first, 
          (float)point.second);
    }

  }
  
  public RoomGeoSmart(Area alreadyBigArea)
  {
    super(getPointsFromArea(alreadyBigArea));
    
    
  }
  
  public RoomGeoSmart(Room r)
  {
    super( r.getPoints());
    this.addAllPoints(r.getPoints());
  }

  public RoomGeoSmart(float [][] points) {
    super(points);
    this.addAllPoints(points);
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

  public void addPointToPolygonAndToShape(float x, float y)
  {
    this.addPointToShape((int) (x * 100), (int)  (y * 100));
    
    Vector3D point = new Vector3D(x*100, x*100, 0);
    
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

  public boolean instersect(RoomGeoSmart r2)
  {
    return this.intersect(r2, intersectionAlgorithm.AREA);
  }

  /**
   * AREA IS BETTER!!
   * @param r2
   * @param algo
   * @return
   */
   public boolean intersect(RoomGeoSmart r2, intersectionAlgorithm algo)
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
   
   public Area getAreaShape100Big()
   {
     return new Area(this.polygon100Big);
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
   
   
   public Vector3D get100BigCentroid() 
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
   
   
   

   public enum intersectionAlgorithm
   {
     INNER_POINTS, AREA
   }

   private static float [][] getPointsFromArea(Area area100Big)
   {
     Polygon polygon100Big = getPolygonFromAreaBig(area100Big);
     return getPointsFromAlreadyBigPolygon(polygon100Big);
   }
   
   private static float [][] getPointsFromAlreadyBigPolygon(Polygon big100Polygon)
   {

     //first  2 points
     float xp1 = big100Polygon.xpoints[0];
     float yp1 = big100Polygon.ypoints[0];
     
     float xp2 = big100Polygon.xpoints[1];
     float yp2 = big100Polygon.ypoints[1];
     
     float [][] pointsStart = new float[][] {{xp1/100, yp1/100}, {xp2/100, yp2/100}};
     
     Room r1 = new Room(pointsStart);
    
     
     for(int i=2; i<big100Polygon.xpoints.length; i++)
     {
       float x = big100Polygon.xpoints[i];
       float y = big100Polygon.xpoints[i];
       r1.addPoint(x/100, y/100);
     }
     
     return r1.getPoints();
   }
   
 private static Polygon getPolygonFromAreaBig(Area area100Big)
 {
   
   Polygon mask_tmp = new Polygon();
   
   PathIterator path = area100Big.getPathIterator(null);
    while (!path.isDone()) 
    {
        toPolygon(path, mask_tmp);
        path.next();
    }
    
   return mask_tmp;
 } 

 private static void toPolygon(PathIterator p_path, Polygon mask_tmp) 
 {
      double[] point = new double[2];
      if(p_path.currentSegment(point) != PathIterator.SEG_CLOSE)
      {
             mask_tmp.addPoint((int) point[0], (int) point[1]);
      }

 }



}
