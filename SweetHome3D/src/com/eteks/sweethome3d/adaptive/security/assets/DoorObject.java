package com.eteks.sweethome3d.adaptive.security.assets;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;


public class DoorObject extends BuildingGraphPart {
  
  private Vector3D position;
  private float angle;
  
  private String idRoom1, idRoom2;
  
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

  public String getIdRoom2() {
    return idRoom2;
  }

  public void setIdRoom2(String idRoom2) {
    this.idRoom2 = idRoom2;
  }

  public String getIdRoom1() {
    return idRoom1;
  }

  public void setIdRoom1(String idRoom1) {
    this.idRoom1 = idRoom1;
  }

 
  
}
