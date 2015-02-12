package com.eteks.sweethome3d.adaptive.security.ifcSecurity;

import ifc2x3javatoolbox.ifc2x3tc1.IfcSpace;
import ifc2x3javatoolbox.ifcmodel.IfcModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingRoomNode;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.UserPreferences;

public abstract class SecurityExtractor {

  protected String ifcFileName;
  protected IfcModel ifcModel;
  protected List<IfcSpace> ifcSpaces = new ArrayList<IfcSpace>();
  protected List<String>   addedWalls = new ArrayList<String>();
  
  protected BuildingSecurityGraph securityGraph = BuildingSecurityGraph.getInstance();
  
  protected Map<IfcSpace, BuildingRoomNode> spaceToRoomNoode  = new HashMap<IfcSpace, BuildingRoomNode>();
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
