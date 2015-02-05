package com.eteks.sweethome3d.model;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public class RoomGeoSmart extends Room {

  private static final long serialVersionUID = 2452728543226021893L;

  private Polygon shape = new Polygon();
  
  public RoomGeoSmart(List<Vector3D> points)
  {
    super(points);
    for(Vector3D point : points)
    {
      this.addPointToRoom((float)point.first, 
                          (float)point.second);
    }
  }
  
  public RoomGeoSmart(float [][] points) {
    super(points);
    for(float [] row : points)
    {
      float x = row[0];
      float y = row[1];
      this.addPointToRoom(x, y);
    }
  }

  public void addPointToRoom(float x, float y)
  {
    this.addPoint(x, y);
    this.addPointToShape((int) (x * 100), (int)  (y * 100));
  }
  
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
   * true iif intersection exist
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
