package com.eteks.sweethome3d.junit.adaptive;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingSecurityGraph;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.adaptive.FurnitureAddTest.ControllerTest;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.SwingViewFactory;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

public class DisplayRoomUnitsSITest extends BasicTest {

  public  static void main(String [] args) {
    ViewFactory viewFactory = new SwingViewFactory();
    UserPreferences preferences = new DefaultUserPreferences();
    preferences.setUnit(LengthUnit.METER);
    Home home = new Home();
    ControllerTest t = new ControllerTest(home, preferences, viewFactory);

    DisplayRoomUnitsSITest f = new DisplayRoomUnitsSITest();
    f.doStuffInsideMain(home, preferences);

  }

  @Override
  public void doStuffInsideMain(Home home, UserPreferences preferences) 
  {
    try 
    {
      openIfcAndGetRooms(home, preferences);
    }
    catch(Exception e) 
    {
      e.printStackTrace();
    }

  }

  public void openIfcAndGetRooms(Home home, UserPreferences preferences) throws Exception
  {
    BuildingSecurityGraph graphScaled = this.openIfcAndReadIt(home, preferences);
    home.displayGraph(graphScaled, preferences);
    
  }
  
    
  
}
