package com.eteks.sweethome3d.junit.adaptive;

import java.util.Map;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.adaptive.security.extractingobjs.IfcSecurityExtractor;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.UserPreferences;

public class IfcExtractorScale extends IfcSecurityExtractor{

  
  
  private float scale;
  public IfcExtractorScale(String ifcFileName, UserPreferences preferences, float scale) {
    
    super(ifcFileName, preferences);
    
    this.scale = scale;
  }
  
  @Override
  protected void setMapOfLibraryObjects(UserPreferences preferences)
  {
    
    super.configLoader = ConfigFileEvilTest.getInstance(preferences);
    
    Map<BuildingObjectType, HomePieceOfFurniture> map =   configLoader.createTypeToFurnitureMap();
    
    preferences.setFornitureMap(map); 
  }
  
  
  
  @Override
  protected float getScaleFactor()
  {
    return this.scale;
  }

}
