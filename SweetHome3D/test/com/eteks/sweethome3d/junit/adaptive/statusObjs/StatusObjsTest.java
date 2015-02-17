package com.eteks.sweethome3d.junit.adaptive.statusObjs;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.PCObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.PrinterObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.FileObject;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.adaptive.BasicTest;
import com.eteks.sweethome3d.junit.adaptive.DisplayRoomUnitsSITest;
import com.eteks.sweethome3d.junit.adaptive.FurnitureAddTest.ControllerTest;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.SwingViewFactory;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

public class StatusObjsTest extends BasicTest {
  
  public void testObjectQuerys()
  {
    Vector3D p1 = new Vector3D(0, 0, 0);
    Vector3D p2 = new Vector3D(100, 20, 0);
     
    PCObject pc1 = new PCObject(p1);
    PrinterObject printer1 = new PrinterObject(p2);
    
    FileObject f1 = new FileObject();
        
    
  }
  
  
  @Override
  public void doStuffInsideMain(Home home, UserPreferences preferences) {
  
  }
  
  public  static void main(String [] args) {
    ViewFactory viewFactory = new SwingViewFactory();
    UserPreferences preferences = new DefaultUserPreferences();
    preferences.setUnit(LengthUnit.METER);
    Home home = new Home();
    ControllerTest t = new ControllerTest(home, preferences, viewFactory);

    StatusObjsTest f = new StatusObjsTest();
    f.doStuffInsideMain(home, preferences);

  }
  
  
}
