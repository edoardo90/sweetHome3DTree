package com.eteks.sweethome3d.adaptive.security.assets;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public class CCTVObject extends MaterialObject {

  public CCTVObject(Vector3D position)
  {
      super(position);
      this.objectType = AssetType.CCTV; 
  }

 

}
