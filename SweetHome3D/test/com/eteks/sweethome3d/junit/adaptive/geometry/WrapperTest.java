package com.eteks.sweethome3d.junit.adaptive.geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.WrapperRect;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.adaptive.tools.btree.BTree;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.adaptive.BasicTest;
import com.eteks.sweethome3d.junit.adaptive.ControllerTest;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.SwingViewFactory;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

public class WrapperTest extends BasicTest {

  public void testBT()
  {
    String s;
    BTree<WrapperRect>  wts;
    
    WrapperRect  cubbyW  = new WrapperRect( cubbyRoom);
    WrapperRect  livingW  = new WrapperRect( livingRoom);
    WrapperRect  diningW  = new WrapperRect( diningRoom);
    WrapperRect  kitchenW  = new WrapperRect( kitchen);
    
     wts = new BTree<WrapperRect>();
    
    wts.insert(cubbyW);
    wts.insert(livingW);
    wts.insert(diningW);
    wts.insert(kitchenW);
    
    WrapperRect p = new WrapperRect(100, 100);
    
    WrapperRect rectOfRoom = wts.getNode(p);
    s = rectOfRoom.getRoomName();
    assertEquals("cubbyRoom", s);
    
    
    wts.insert(cubbyW);
    wts.insert(diningW);
    wts.insert(cubbyW);
    
    WrapperRect rectRoom2  = wts.getNode(p);
    s = rectRoom2.getRoomId();
    assertEquals("cubbyRoom", s);
    
    
    
  }
  
  
  public void testWrappCubby()
  {
    prepareHome(home, preferences);
    
    WrapperRect  cubbyW  = new WrapperRect( cubbyRoom.getBoundingRoomRect3D());
    WrapperRect  livingW  = new WrapperRect( livingRoom.getBoundingRoomRect3D());
    WrapperRect  diningW  = new WrapperRect( diningRoom.getBoundingRoomRect3D());
    WrapperRect  kitchenW  = new WrapperRect( kitchen.getBoundingRoomRect3D());
    
    
    TreeSet<WrapperRect> rects = new TreeSet<WrapperRect>();
    List<WrapperRect> rectsList = new ArrayList<WrapperRect>();
    
    TreeMap<WrapperRect, String> rectsTreeMap = new TreeMap<WrapperRect, String>();
 
    rectsList.add(cubbyW);
    rectsList.add(livingW);
    rectsList.add(diningW);
    rectsList.add(kitchenW);
    
    rectsTreeMap.put(cubbyW, "");
    rectsTreeMap.put(livingW, "");
    rectsTreeMap.put(diningW, "");
    rectsTreeMap.put(kitchenW, "");

    
    
    rects.addAll(rectsList);
    System.out.println("list: " + rectsList);
    System.out.println("\n\nordered list : " + rects);
    System.out.println("_____________________");
    
    Vector3D inCubby = new Vector3D(100, 100, 0);
    
    boolean found = false;
    for(WrapperRect w : rects)
    {
      if(w.equals(inCubby))
      {
        System.out.println("W : "  + w);
        assertTrue("the wrapper containing point inside cubby should be the cubby wrapper",
            w.equals(cubbyW));
        found = true;
      }
    }
    
    assertTrue(found);
    
  }
  
  @Override
  public void doStuffInsideMain(Home home, UserPreferences preferences) {
    super.setUp();
    
    this.testWrappCubby();
  }
  
  public  static void main(String [] args) {
    ViewFactory viewFactory = new SwingViewFactory();
    UserPreferences preferences = new DefaultUserPreferences();
    preferences.setUnit(LengthUnit.METER);
    Home home = new Home();
    ControllerTest t = new ControllerTest(home, preferences, viewFactory);
    WrapperTest htg = new WrapperTest();
    htg.doStuffInsideMain(home, preferences);
    
    
  }
  

}
