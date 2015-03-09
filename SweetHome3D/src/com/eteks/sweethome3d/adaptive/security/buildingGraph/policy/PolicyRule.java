package com.eteks.sweethome3d.adaptive.security.buildingGraph.policy;

import java.io.Serializable;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.MaterialObject;

public class PolicyRule implements Serializable {
  
  private MaterialObject materialObject;
  private Role role;
  private ActorAction action;
  public MaterialObject getMaterialObject() {
    return materialObject;
  }
  public void setMaterialObject(MaterialObject object) {
    this.materialObject = object;
  }
  public Role getRole() {
    return role;
  }
  public void setRole(Role role) {
    this.role = role;
  }
  public ActorAction getAction() {
    return action;
  }
  public void setAction(ActorAction action) {
    this.action = action;
  }
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.action == null)
        ? 0
        : this.action.hashCode());
    result = prime * result + ((this.materialObject == null)
        ? 0
        : this.materialObject.hashCode());
    result = prime * result + ((this.role == null)
        ? 0
        : this.role.hashCode());
    return result;
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PolicyRule other = (PolicyRule)obj;
    if (this.action != other.action)
      return false;
    if (this.materialObject == null) {
      if (other.materialObject != null)
        return false;
    } else if (!this.materialObject.equals(other.materialObject))
      return false;
    if (this.role == null) {
      if (other.role != null)
        return false;
    } else if (!this.role.equals(other.role))
      return false;
    return true;
  }
  
  
  
  

}
