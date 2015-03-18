package com.eteks.sweethome3d.junit.adaptive;

import com.eteks.sweethome3d.adaptive.security.assets.BuildingObjectType;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
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
////////////    errors !  home.displayGraph(graphScaled, preferences);
//////////  
////////    
//////
////    
//

     
    ConfigFileEvilTest cfg = ConfigFileEvilTest.getInstance(preferences);
    String nameOfCCTV =  cfg.getSweetHomeNameForType(BuildingObjectType.CCTV);
    System.out.println("cctv : " + nameOfCCTV);   //Doccia
    String objectName = "Doccia";
    BuildingObjectType typeOfObject = cfg.getTypeForSweetHomeName(objectName);
    System.out.println(" type  :" + typeOfObject);
    
  }
  
    
  
}
