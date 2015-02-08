package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;


public class DoorObject extends BuildingGraphPart {
  
  private Vector3D position;
  private float angle;
  
  public Vector3D getPosition() {
    return position;
  }

  public void setPosition(Vector3D position) {
    this.position = position;
  }

  public float getAngle() {
    return angle;
  }

  public void setAngle(float angle) {
    this.angle = angle;
  }

 
  
}
