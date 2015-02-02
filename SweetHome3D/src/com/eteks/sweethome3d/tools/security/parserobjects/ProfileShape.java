package com.eteks.sweethome3d.tools.security.parserobjects;

import java.util.List;

public class ProfileShape extends Shape3D {

  protected List<Vector3D> points;

  public ProfileShape(List<Vector3D> points) 
  {
    this.points = points;
  }
  
  @Override
  public List<Vector3D> getListOfPoints() {
    return points;
  }
  
  
}
