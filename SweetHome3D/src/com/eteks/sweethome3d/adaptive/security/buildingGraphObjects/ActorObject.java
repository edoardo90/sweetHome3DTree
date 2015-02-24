package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.policy.Role;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.swing.objstatus.StatusOfObjectForView;




public class ActorObject extends BuildingObjectContained {
  
  private List<Role> roles = new ArrayList<Role>();
  
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

  public List<Role> getRoles() {
    return roles;
  }

  public void setRole(List<Role> roles) {
    this.roles = roles;
  }
  
  public void addRole(Role role)
  {
    this.roles.add(role);
  }


}
