package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.UserPreferences;

public abstract class BuildingObjectContained extends BuildingGraphPart {

  protected BuildingObjectType objectType;
  private Vector3D position;
  
  public BuildingObjectContained(Vector3D position) {
    this.setPosition(position);
  }
 
  /**
   * The 3D representation of the object wrapped inside the SweetHome HomePiece object
   * @param preferences
   * @return
   */
  public  HomePieceOfFurniture getPieceOfForniture(UserPreferences preferences)
  {
    return preferences.getPieceOfForniture(objectType);
  }
  
  @Override
  public String toString()
  {
    return (this.objectType != null ? this.objectType.toString() : "object")  +
           "ID:" + this.getId();
           
  }
  
  public String typeString()
  {
    return "" + this.objectType;
  }
  

  public Vector3D getPosition() {
    return position;
  }

  public void setPosition(Vector3D position) {
    this.position = position;
  }
  
  
  
}
