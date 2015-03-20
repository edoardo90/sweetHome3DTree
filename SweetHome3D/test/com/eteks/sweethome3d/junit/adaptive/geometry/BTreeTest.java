package com.eteks.sweethome3d.junit.adaptive.geometry;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.WrapperRect;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Rectangle3D;
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

public class BTreeTest extends BasicTest {

  private boolean homePrepared = false;
  public void testBTreeWraps()
  {
    
    WrapperRect  cubbyW  = new WrapperRect( cubbyRoom);
    WrapperRect  livingW  = new WrapperRect( livingRoom);
    WrapperRect  diningW  = new WrapperRect( diningRoom);
    WrapperRect  kitchenW  = new WrapperRect( kitchen);
    
    BTree<WrapperRect> rectsTreeMap = new BTree<WrapperRect>();
    
    rectsTreeMap.insert(cubbyW);
    rectsTreeMap.insert(livingW);
    rectsTreeMap.insert(diningW);
    rectsTreeMap.insert(kitchenW);

    Vector3D inCubby = new Vector3D(100, 100, 0);
    boolean found = false;
    
    WrapperRect wrapKey = new WrapperRect(new Rectangle3D(inCubby, 10, 10));
    
    WrapperRect description = rectsTreeMap.getNode(wrapKey);
    System.out.println("\n\n ============== \n description :" + description.getRoomName());


    

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
