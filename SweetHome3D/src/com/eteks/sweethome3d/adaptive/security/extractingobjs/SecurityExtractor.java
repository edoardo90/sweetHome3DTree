package com.eteks.sweethome3d.adaptive.security.extractingobjs;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.model.UserPreferences;

public abstract class SecurityExtractor {

  protected BuildingSecurityGraph securityGraph = BuildingSecurityGraph.getInstance();
  
  protected ConfigLoader configLoader;
  protected UserPreferences preferences;
  
  public SecurityExtractor( UserPreferences preferences)
  {
    this.securityGraph.clearAll();
    this.configLoader = this.getConfig(preferences); 
    this.preferences = preferences;
  }
  
  
  protected abstract  ConfigLoader getConfig(UserPreferences preferences);
  public abstract BuildingSecurityGraph getGraph() throws Exception ;
  
  
}
