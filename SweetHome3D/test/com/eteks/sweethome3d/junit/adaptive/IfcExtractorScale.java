package com.eteks.sweethome3d.junit.adaptive;

import com.eteks.sweethome3d.adaptive.security.extractingobjs.SavedConfigurationsLoader;
import com.eteks.sweethome3d.adaptive.security.extractingobjs.IfcSecurityExtractor;
import com.eteks.sweethome3d.model.UserPreferences;

public class IfcExtractorScale extends IfcSecurityExtractor{

  
  
  private float scale;
  public IfcExtractorScale(String ifcFileName, UserPreferences preferences, float scale) {
    
    super(ifcFileName, preferences);
    
    this.scale = scale;
  }
  
  @Override
  protected SavedConfigurationsLoader getConfig(UserPreferences preferences) {
    return ConfigFileEvilTest.getInstance(preferences);
  }
  
  @Override
  protected float getScaleFactor()
  {
    return this.scale;
  }

}
