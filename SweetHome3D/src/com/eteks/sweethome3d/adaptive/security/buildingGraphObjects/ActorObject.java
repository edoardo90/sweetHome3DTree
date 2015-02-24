package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.policy.Role;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.swing.objstatus.StatusOfObjectForView;




public class ActorObject extends BuildingObjectContained {
  
  private Role role;
  
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

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }


}
