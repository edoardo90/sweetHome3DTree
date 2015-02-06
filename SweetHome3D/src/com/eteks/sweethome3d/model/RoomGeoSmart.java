package com.eteks.sweethome3d.model;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Rectangle3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Shape3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public class RoomGeoSmart extends Room {

  private static final long serialVersionUID = 2452728543226021893L;

  private Polygon shape = new Polygon();

  public RoomGeoSmart(List<Vector3D> points)
  {
    super(points);
    for(Vector3D point : points)
    {
      this.addPointToPolygon((float)point.first, 
          (float)point.second);
    }
  }

  public RoomGeoSmart(Shape3D shape3D)
  {
    super(shape3D.getListOfPoints());
    for(Vector3D point : shape3D.getListOfPoints())
    {
      this.addPointToPolygon((float)point.first, 
          (float)point.second);
    }

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
      this.addPointToPolygon(x, y);
    }
  }

  public void addPointToPolygon(float x, float y)
  {
    this.addPointToShape((int) (x * 100), (int)  (y * 100));
  }

  public Rectangle3D getBoundingRect3d()
  {
    Rectangle2D rect =  this.shape.getBounds2D();
    double xc = rect.getCenterX();
    double yc = rect.getCenterY();

    double height = rect.getHeight();
    double width =  rect.getWidth();

    Rectangle3D rect3D = new Rectangle3D(new Vector3D(xc, yc, 0), width, height);
    return rect3D;
  }

  public RoomGeoSmart getBiggerRoomBordered(float borderSize)
  {
    Rectangle2D rect =  this.shape.getBounds2D();
    double xc = rect.getCenterX();
    double yc = rect.getCenterY();

    double height = rect.getHeight();
    double width =  rect.getWidth();

    Rectangle3D rect3D = new Rectangle3D(new Vector3D(xc, yc, 0), width, height);
    float[][] points = this.getPoints();
    for(float[] row : points)
    {
      float xp = row[0];
      float yp = row[1];
      if(xp > xc)
        xp = xp + borderSize;
      else if(xp > xc)
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

    Polygon p1 = this.shape;
    Polygon p2 = r2.shape;

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
     this.shape.addPoint(x , y );
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

   public enum intersectionAlgorithm
   {
     INNER_POINTS, AREA
   }



}
