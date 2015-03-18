package com.eteks.sweethome3d.junit.adaptive;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.eteks.sweethome3d.adaptive.security.extractingobjs.SavedConfigurationsLoader;
import com.eteks.sweethome3d.junit.resources.ResTest;
import com.eteks.sweethome3d.model.UserPreferences;


public class ConfigFileEvilTest extends SavedConfigurationsLoader {

private static ConfigFileEvilTest instance = null;
  
  public static ConfigFileEvilTest getInstance(UserPreferences preferences)
  {

    if(instance == null)
    {
      instance = new ConfigFileEvilTest(preferences);
      return instance;
    }
    else
    {
      return instance;
    }
  }

  
  private ConfigFileEvilTest(UserPreferences preferences)
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
      if(url == null)
      {
        int pippo=0;
        pippo++;
      }
      uri = url.toURI();
    } catch (URISyntaxException ex) {

      ex.printStackTrace();
    }
    File file = new File(uri);
    return file;
  }
  
}
