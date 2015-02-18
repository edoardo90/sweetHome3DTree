package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.swing.objstatus.FrameStatus.StatusOfObjectForView;




public class ActorObject extends BuildingObjectContained {

  public ActorObject(Vector3D position)
  {
    super(position);
    this.objectType = BuildingObjectType.ACTOR;
  }

  @Override
  public StatusOfObjectForView getStatusForView() {
    StatusOfObjectForView st = new StatusOfObjectForView("", null);
    return st;
  }

  @Override
  public void setStatusFromView(StatusOfObjectForView s) {
    // TODO actor has no real state
  }


}
