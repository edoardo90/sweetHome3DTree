package com.eteks.sweethome3d.junit;

import com.eteks.sweethome3d.adaptive.security.extractingobjs.SavedConfigurationsLoader;
import com.eteks.sweethome3d.adaptive.security.extractingobjs.HomeSecurityExtractor;
import com.eteks.sweethome3d.junit.adaptive.ConfigFileEvilTest;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.UserPreferences;

public class HomeExtractorEvil extends HomeSecurityExtractor {

  public HomeExtractorEvil(Home home, UserPreferences preferences) {
    super(home, preferences);
   
  }
  
  @Override
  protected SavedConfigurationsLoader getConfig(UserPreferences preferences) {
    return ConfigFileEvilTest.getInstance(preferences);
  }

}
