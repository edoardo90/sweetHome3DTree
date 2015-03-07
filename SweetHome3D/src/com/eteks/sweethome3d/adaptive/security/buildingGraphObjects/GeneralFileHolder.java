package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public class GeneralFileHolder extends FileHolder {

  public GeneralFileHolder(Vector3D position) {
    super(position);
    this.objectType = BuildingObjectType.GENERAL_FILE_HOLEDER;
  }

}
