package com.eteks.sweethome3d.junit.adaptive.graphhome;

import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.adaptive.BasicTest;
import com.eteks.sweethome3d.junit.adaptive.ControllerTest;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.SwingViewFactory;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

public class HomeToGraphTest extends BasicTest {

  
  public  static void main(String [] args) {
    ViewFactory viewFactory = new SwingViewFactory();
    UserPreferences preferences = new DefaultUserPreferences();
    preferences.setUnit(LengthUnit.METER);
    Home home = new Home();
    ControllerTest t = new ControllerTest(home, preferences, viewFactory);
    HomeToGraphTest htg = new HomeToGraphTest();
    htg.doStuffInsideMain(home, preferences);
  }
  
  @Override
  public void doStuffInsideMain(Home home, UserPreferences preferences) {
    
    
  }
  
  


  

  
  
}
