package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public class HVACObject extends BuildingObjectContained{

  public HVACObject(Vector3D position)
  {
    super(position);
    this.objectType = objectType.HVAC;
  }
}
