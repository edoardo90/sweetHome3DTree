package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public class CCTVObject extends BuildingObjectContained {

  public CCTVObject(Vector3D position)
  {
      super(position);
      this.objectType = BuildingObjectType.CCTV; 
  }
 

}
