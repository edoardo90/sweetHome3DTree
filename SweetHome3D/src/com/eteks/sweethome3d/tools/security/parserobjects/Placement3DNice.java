package com.eteks.sweethome3d.tools.security.parserobjects;



public class Placement3DNice
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
