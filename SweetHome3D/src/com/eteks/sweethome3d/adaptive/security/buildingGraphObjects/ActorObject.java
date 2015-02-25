package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

  public Collection<? extends String> getRolesStr() {
    Set<String> roless = new HashSet<String>();
    for(Role r : this.roles)
    {
      roless.add(r.getRole());
    }
    return roless;
  }

  public void setRolesStr(Set<String> selectedRoles) {
    this.roles.clear();
    for(String roleStr : selectedRoles)
    {
      this.addRole(new Role(roleStr));
    }
    
  }


}
