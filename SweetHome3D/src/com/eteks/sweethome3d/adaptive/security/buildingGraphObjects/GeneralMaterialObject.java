package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public class GeneralMaterialObject extends MaterialObject {

  public GeneralMaterialObject(Vector3D position) {
    super(position);
    this.objectType = BuildingObjectType.GENERAL_MATERIAL_OBJ;
  }

}
