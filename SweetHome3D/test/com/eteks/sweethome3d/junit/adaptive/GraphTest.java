package com.eteks.sweethome3d.junit.adaptive;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.ifcSecurity.GraphClean;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.adaptive.FurnitureAddTest.ControllerTest;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.SwingViewFactory;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

public class GraphTest extends BasicTest {



  public  static void main(String [] args) {
    ViewFactory viewFactory = new SwingViewFactory();
    UserPreferences preferences = new DefaultUserPreferences();
    preferences.setUnit(LengthUnit.METER);
    Home home = new Home();
    ControllerTest t = new ControllerTest(home, preferences, viewFactory);

    GraphTest f = new GraphTest();
    f.doStuffInsideMain(home, preferences);

  }

  @Override
  public void doStuffInsideMain(Home home, UserPreferences preferences) {
    try
    {
      BuildingSecurityGraph  securityGraph = 
          super.openIfcAndReadIt(home, preferences, "5 rooms with objects.ifc");
      
      GraphClean gc = new GraphClean(securityGraph);
      gc.populateGraph();
      System.out.println("woo");
      gc.show();
      
    } 
    catch (Exception ex) {

      ex.printStackTrace();
    }

  }

}
