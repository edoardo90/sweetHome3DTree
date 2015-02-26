package com.eteks.sweethome3d.junit.adaptive.geometry;

import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Rectangle3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.adaptive.BasicTest;
import com.eteks.sweethome3d.junit.adaptive.ControllerTest;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.RoomGeoSmart;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.SwingViewFactory;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

public class RectangleTest extends BasicTest {
  
  List<Rectangle3D> rectanglesForTest = new ArrayList<Rectangle3D>();
  
  public void testRectArea()
  {
    Rectangle3D r1 = new Rectangle3D(new Vector3D(), 100, 200);
    r1.setRectName("r1");
    
    this.rectanglesForTest.add(r1);
    assertTrue(r1.getArea() == 100 * 200);
  }
  
  public void testRectNSWE()
  {
    Vector3D nW = new Vector3D(100, 0, 0);
    Vector3D nE = new Vector3D(600, 0, 0);
    
    Vector3D sW = new Vector3D(100, 300, 0);
    Vector3D sE = new Vector3D(600, 300, 0);
    
    Rectangle3D r2 = new Rectangle3D(nE, nW, sW, sE);
    r2.setRectName("r2");
    
    this.rectanglesForTest.add(r2);
    assertTrue(r2.isARectangle());
    
    Rectangle3D r3 = new Rectangle3D(nE, sW, nW, sE);
    r3.setRectName("r3");
    this.rectanglesForTest.add(r3);
    assertTrue(r3.isARectangle());
    
    
    
  }
  

  public  static void main(String [] args) {
    ViewFactory viewFactory = new SwingViewFactory();
    UserPreferences preferences = new DefaultUserPreferences();
    preferences.setUnit(LengthUnit.METER);
    Home home = new Home();
    ControllerTest t = new ControllerTest(home, preferences, viewFactory);
    RectangleTest rt = new RectangleTest();
    rt.doStuffInsideMain(home, preferences);
  }
  
  private void addAllRectAsRoomsGS()
  {
    for(Rectangle3D r : this.rectanglesForTest)
    {
      RoomGeoSmart rgs = new RoomGeoSmart(r);
      rgs.setName(r.getRectName());
      home.addRoom(rgs);
    }
  }
  
  
  @Override
  public void doStuffInsideMain(Home home, UserPreferences preferences) {
      this.home = home;
      this.runAllOwnTests();
      this.addAllRectAsRoomsGS();
      
  }
  
  
}
