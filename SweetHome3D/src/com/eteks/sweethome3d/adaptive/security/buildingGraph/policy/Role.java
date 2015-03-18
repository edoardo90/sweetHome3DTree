package com.eteks.sweethome3d.adaptive.security.buildingGraph.policy;

import java.io.Serializable;

import com.eteks.sweethome3d.adaptive.security.extractingobjs.SavedConfigurationsLoader;


public class Role implements Serializable {
  private String role;

  public Role(String roleStr) {
    if(SavedConfigurationsLoader.getInstance().isARole(roleStr))
    {
      this.role = roleStr;
    }
    else
    {
      throw new IllegalStateException("role:" + roleStr + " is not a valid role ");
    }
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    
    this.role = role;
  }
  @Override
  public String toString()
  {
    return "ROLE:" + this.getRole();
  }
  
  
}
