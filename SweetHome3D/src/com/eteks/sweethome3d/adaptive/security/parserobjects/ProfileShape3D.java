package com.eteks.sweethome3d.adaptive.security.parserobjects;

import java.util.ArrayList;
import java.util.List;

public class ProfileShape3D extends Shape3D {

  protected List<Vector3D> points;

  public ProfileShape3D(List<Vector3D> points) 
  {
    this.points = points;
  }
  
  public void addPoint(Vector3D point)
  {
    this.points.add(point);
  }
  
  
  @Override
  public List<Vector3D> getListOfPoints()
  {
    List<Vector3D> lstToGet = new ArrayList<Vector3D>();
    for(Vector3D point : points)
    {
      lstToGet.add( point);
    }
    return lstToGet;
  }

  @Override
  public void scale(float scaleFactor)
  {
    
    for(Vector3D p : this.points)
    {
      p.scale(scaleFactor);
    }
    
  }
  
  
}
