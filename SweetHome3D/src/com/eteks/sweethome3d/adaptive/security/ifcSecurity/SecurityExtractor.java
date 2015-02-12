package com.eteks.sweethome3d.adaptive.security.ifcSecurity;

import java.util.Map;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.UserPreferences;

public abstract class SecurityExtractor {

  protected BuildingSecurityGraph securityGraph = BuildingSecurityGraph.getInstance();
  
  protected ConfigLoader configLoader;
  protected UserPreferences preferences;

  public SecurityExtractor( UserPreferences preferences)
  {
    this.securityGraph.clearAll();
    this.configLoader = ConfigLoader.getInstance(preferences); 
    this.setMapOfLibraryObjects(preferences);
    this.preferences = preferences;
  }

  protected void setMapOfLibraryObjects(UserPreferences preferences)
  {
    Map<BuildingObjectType, HomePieceOfFurniture> map =   configLoader.createTypeToFurnitureMap();
    preferences.setFornitureMap(map); 
  }
  
  public abstract BuildingSecurityGraph getGraph() throws Exception ;
  
  
}
