package com.eteks.sweethome3d.junit.adaptive.geometry;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BTree;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.WrapperRect;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Rectangle3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.adaptive.BasicTest;
import com.eteks.sweethome3d.junit.adaptive.ControllerTest;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.SwingViewFactory;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

public class BTreeTest extends BasicTest {

  private boolean homePrepared = false;
  public void testBTreeWraps()
  {
    
    WrapperRect  cubbyW  = new WrapperRect( cubbyRoom.getBoundingRoomRect3D());
    WrapperRect  livingW  = new WrapperRect( livingRoom.getBoundingRoomRect3D());
    WrapperRect  diningW  = new WrapperRect( diningRoom.getBoundingRoomRect3D());
    WrapperRect  kitchenW  = new WrapperRect( kitchen.getBoundingRoomRect3D());
    
    BTree<WrapperRect, String> rectsTreeMap = new BTree<WrapperRect, String>();
    
    rectsTreeMap.put(cubbyW, "cubby room");
    rectsTreeMap.put(livingW, "living room");
    rectsTreeMap.put(diningW, "dinin' room");
    rectsTreeMap.put(kitchenW, "kitchen ");

    Vector3D inCubby = new Vector3D(100, 100, 0);
    boolean found = false;
    
    WrapperRect wrapKey = new WrapperRect(new Rectangle3D(inCubby, 10, 10));
    
    String description = rectsTreeMap.get(wrapKey);
    System.out.println("\n\n ============== \n description :" + description);


    

  }

  public  void testTryBTreeStrings()
  {
      BTree<String, String> st = new BTree<String, String>();

//    st.put("www.cs.princeton.edu", "128.112.136.12");
      st.put("www.cs.princeton.edu", "128.112.136.11");
      st.put("www.princeton.edu",    "128.112.128.15");
      st.put("www.yale.edu",         "130.132.143.21");
      st.put("www.simpsons.com",     "209.052.165.60");
      st.put("www.apple.com",        "17.112.152.32");
      st.put("www.amazon.com",       "207.171.182.16");
      st.put("www.ebay.com",         "66.135.192.87");
      st.put("www.cnn.com",          "64.236.16.20");
      st.put("www.google.com",       "216.239.41.99");
      st.put("www.nytimes.com",      "199.239.136.200");
      st.put("www.microsoft.com",    "207.126.99.140");
      st.put("www.dell.com",         "143.166.224.230");
      st.put("www.slashdot.org",     "66.35.250.151");
      st.put("www.espn.com",         "199.181.135.201");
      st.put("www.weather.com",      "63.111.66.11");
      st.put("www.yahoo.com",        "216.109.118.65");

      
      
      
      System.out.println("cs.princeton.edu:  " + st.get("www.cs.princeton.edu"));
      System.out.println("hardvardsucks.com: " + st.get("www.harvardsucks.com"));
      System.out.println("simpsons.com:      " + st.get("www.simpsons.com"));
      System.out.println("apple.com:         " + st.get("www.apple.com"));
      System.out.println("ebay.com:          " + st.get("www.ebay.com"));
      System.out.println("dell.com:          " + st.get("www.dell.com"));
      System.out.println();

      System.out.println("size:    " + st.size());
      System.out.println("height:  " + st.height());
      System.out.println(st);
      System.out.println();
  }


  @Override
  public void doStuffInsideMain(Home home, UserPreferences preferences)
  {
    super.setUp();
    this.prepareHome(home, preferences);
    this.homePrepared = true;
    ////this.testTryBTreeStrings();
    
    this.testBTreeWraps();
    
  }

  public  static void main(String [] args) {
   
    ViewFactory viewFactory = new SwingViewFactory();
    UserPreferences preferences = new DefaultUserPreferences();
    preferences.setUnit(LengthUnit.METER);
    Home home = new Home();
    ControllerTest t = new ControllerTest(home, preferences, viewFactory);
    
    BTreeTest htg = new BTreeTest();
    htg.doStuffInsideMain(home, preferences);
    
    
    
  }
  
}
