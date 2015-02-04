package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.UserPreferences;

public abstract class BuildingObjectContained extends BuildingGraphPart {

  protected BuildingObjectType objectType;
  
  public BuildingObjectContained(Vector3D position) {
    super(position);

  }
 
  public  HomePieceOfFurniture getPieceOfForniture(UserPreferences preferences)
  {
    return preferences.getPieceOfForniture(objectType);
  }
  
  @Override
  public String toString()
  {
    return this.objectType != null ? this.objectType.toString() : "object";
  }
  
  
}
