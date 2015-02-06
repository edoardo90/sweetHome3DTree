package com.eteks.sweethome3d.junit.adaptive;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.eteks.sweethome3d.adaptive.security.ifcSecurity.ConfigLoader;
import com.eteks.sweethome3d.junit.resources.ResTest;
import com.eteks.sweethome3d.model.UserPreferences;


public class ConfigFileTest extends ConfigLoader {

private static ConfigLoader instance = null;
  
  public static ConfigLoader getInstance(UserPreferences preferences)
  {

    if(instance == null)
    {
      instance = new ConfigFileTest(preferences);
      return instance;
    }
    else
    {
      return instance;
    }
  }

  
  private ConfigFileTest(UserPreferences preferences)
  {
    super(preferences);
  }
  
  @Override
  protected File getFileFromName(String name)
  {
    Class<ResTest> classe =ResTest.class;
    URL url = classe.getResource(name);
    URI uri=null;
    try {
      uri = url.toURI();
    } catch (URISyntaxException ex) {

      ex.printStackTrace();
    }
    File file = new File(uri);
    return file;
  }
  
}
