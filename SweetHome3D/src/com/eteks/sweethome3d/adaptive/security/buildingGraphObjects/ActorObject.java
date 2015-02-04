package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;



public class ActorObject extends BuildingObjectContained {

  public ActorObject(Vector3D position)
  {
    super(position);
    this.objectType = BuildingObjectType.ACTOR;
  }


}
