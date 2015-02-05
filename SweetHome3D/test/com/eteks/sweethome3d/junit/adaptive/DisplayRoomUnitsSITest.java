package com.eteks.sweethome3d.junit.adaptive;

import java.io.File;
import java.net.URI;
import java.net.URL;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.ifcSecurity.IfcSecurityExtractor;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.OBJWriterTest;
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
    String ifcFileName = "";
    Class<OBJWriterTest> classe =OBJWriterTest.class;
    URL url = classe.getResource("resources/ifcfiles/5 rooms with objects.ifc");
    URI uri = url.toURI();
    File file = new File(uri);
    ifcFileName = file.getAbsolutePath();

    
    IfcSecurityExtractor extractor= null;
    
    try{
      extractor = new IfcExtractorScale(ifcFileName, preferences, 1f);
    }
    catch(Exception e)
    {
      System.out.println(e.getStackTrace());
    }

    BuildingSecurityGraph graph = extractor.getGraphFromFile();
    home.displayGraph(graph, preferences);
    
    IfcSecurityExtractor extractorScaled = new IfcExtractorScale(ifcFileName, preferences, 2f);

    BuildingSecurityGraph graphScaled = extractorScaled.getGraphFromFile();
    home.displayGraph(graphScaled, preferences);
    
    
    
  }
  
  
  
  
  
  
  



}
