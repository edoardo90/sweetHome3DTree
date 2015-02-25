package com.eteks.sweethome3d.adaptive.security.buildingGraph.policy;

import com.eteks.sweethome3d.adaptive.security.extractingobjs.ConfigLoader;


public class Role {
  private String role;

  public Role(String roleStr) {
    if(ConfigLoader.getInstance().isARole(roleStr))
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
