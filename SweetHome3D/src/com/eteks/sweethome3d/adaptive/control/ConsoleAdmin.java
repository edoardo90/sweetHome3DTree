package com.eteks.sweethome3d.adaptive.control;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.FileObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.LifeStatus;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public interface ConsoleAdmin {

  public void addOjbect(String idRoom, BuildingObjectType type);
  
  public void addOjbect(Vector3D absoluteCoordinates, BuildingObjectType type);
  
  public void moveObject(String idObject, String idRoom);
  
  public void moveObject(String idObject, Vector3D coordinates);
  
  public void destroyObject(String idObject);
  
  public void changeObjectLife(String idObject, LifeStatus status);
  
  public void addFileToPeriferical(String idObject, FileObject file);
  
  public void destroyFile(String idFile);
  
  public void createLink(String idObject1, String idObject2);
  
  
}
