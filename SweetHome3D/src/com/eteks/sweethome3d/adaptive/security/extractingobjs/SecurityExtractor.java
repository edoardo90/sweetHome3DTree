package com.eteks.sweethome3d.adaptive.security.extractingobjs;

import java.util.Map;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
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
  
  /**
   * Create a map from object type to homePieceOfFurniture, this map is used by the objects
   * This is used by objects of the class {@link BuildingObjectContained} to call getPieceOfForniture
   * @param preferences
   */
  protected void setMapOfLibraryObjects(UserPreferences preferences)
  {
    Map<BuildingObjectType, HomePieceOfFurniture> map =   configLoader.createTypeToFurnitureMap();
    preferences.setFornitureMap(map); 
  }
  
  public abstract BuildingSecurityGraph getGraph() throws Exception ;
  
  
}
