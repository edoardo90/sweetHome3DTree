package com.eteks.sweethome3d.adaptive.security.assets;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public class HVACObject extends MaterialObject{

  public HVACObject(Vector3D position)
  {
    super(position);
    this.objectType = objectType.HVAC;
  }
}
