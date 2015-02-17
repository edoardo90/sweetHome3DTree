package com.eteks.sweethome3d.junit.adaptive.graphhome;

import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingRoomNode;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.adaptive.security.extractingobjs.HomeSecurityExtractor;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.adaptive.BasicTest;
import com.eteks.sweethome3d.junit.adaptive.ControllerTest;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.SwingViewFactory;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

public class HomeToGraphTest extends BasicTest {



  public void setUp()
  {
    super.setUp();

    if(home == null)
      home = new Home();

    if(preferences == null)
      preferences = new DefaultUserPreferences();
    this.prepareHome(home, preferences);

  }

  public void testMove() throws Exception
  {

    HomeSecurityExtractor hse = new HomeSecurityExtractor(home, preferences);

    BuildingSecurityGraph segraph =  hse.getGraph();

    segraph.moveObject("washing", this.kitchen.getId());

    assertTrue("the object has been moved",
        containsDeep(segraph, this.kitchen.getId(), "washing"));

    assertFalse("the object should be only in kitchen",
        containsDeep(segraph, this.cubbyRoom.getId(), "washing"));

    assertFalse("the object should be only in kitchen",
        containsDeep(segraph, this.diningRoom.getId(), "washing"));

    assertFalse("the object should be only in kitchen",
        containsDeep(segraph, this.livingRoom.getId(), "washing"));



  }

  public void testRemove() throws Exception
  {
    HomePieceOfFurniture hopf = super.getHomePiece(preferences, 1, 2, "wash", 100, 100);
    home.addPieceOfFurniture(hopf);

    HomeSecurityExtractor hse = new HomeSecurityExtractor(home, preferences);
    BuildingSecurityGraph segraph =  hse.getGraph();

    segraph.removeObject("wash");

    assertFalse("there should not be anymore!",
        this.containsDeep(segraph, this.cubbyRoom.getId(), "wash"));

  }


  public void testAdd() throws Exception
  {

    HomePieceOfFurniture hopf = super.getHomePiece(preferences, 1, 2, "wash", 100, 100);
    home.addPieceOfFurniture(hopf);

    HomeSecurityExtractor hse = new HomeSecurityExtractor(home, preferences);
    BuildingSecurityGraph segraph =  hse.getGraph();

    
    assertTrue("there should be wash in cubby room",
        this.containsDeep(segraph, this.cubbyRoom.getId(), "wash"));

    segraph.addNewObject("light1", BuildingObjectType.LIGHT, "kitchen", 
        new Vector3D(300, 400, 0));


    assertTrue("light1 should be in kitchen", 
        this.containsDeep(segraph, this.kitchen.getId(), "light1"));

    segraph.moveObject("light1", new Vector3D(700, 150, 0));

    assertTrue("light1 has been moved in livingRoom", 
        this.containsDeep(segraph, this.livingRoom.getId(), "light1"));



  }

  public void testMatchCoords() throws Exception
  {
    HomeSecurityExtractor hse = new HomeSecurityExtractor(home, preferences);
    BuildingSecurityGraph segraph =  hse.getGraph();

    String idRoom  = segraph.getRoomId(new Vector3D(300, 400, 0));
    assertEquals("should be kitchen id", this.kitchen.getId(), idRoom);

    idRoom  = segraph.getRoomId(new Vector3D(100, 100, 0));
    assertEquals("should be cubby id", this.cubbyRoom.getId(), idRoom);

    idRoom  = segraph.getRoomId(new Vector3D(800, 300, 0));
    assertEquals("should be dining id", this.diningRoom.getId(), idRoom);

    idRoom  = segraph.getRoomId(new Vector3D(300, 100, 0));
    assertEquals("should be living id", this.livingRoom.getId(), idRoom);
    
    x();
    
  }

  

  private void x()
  {

    if(home != null)
    {
      if(home.getFurniture() != null) {
        for(HomePieceOfFurniture h : home.getFurniture())
        {
          System.out.println("furn ID: " + h.getId() + "\n furn name: " + h.getName());
        
        }
     }
    }

  }



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

    this.setUp();
    prepareHome(home , preferences);
    HomeSecurityExtractor hse = new HomeSecurityExtractor(home, preferences);
    try 
    {   BuildingSecurityGraph segraph =  hse.getGraph();
    
        segraph.moveObject("washing", "kitchen");
    
    }
    catch (Exception ex)  {     ex.printStackTrace();  }

  }

  protected boolean containsDeep(BuildingSecurityGraph segraph, String roomId, String objectId)
  {

    List<BuildingRoomNode> roomNodes = segraph.getRoomNodeList();

    for(BuildingRoomNode roomNode : roomNodes)
    {
      List<BuildingObjectContained> contained = roomNode.getObjectsInside();
      for(BuildingObjectContained obj : contained)
      {
        if(obj.getId().equals(objectId) && roomNode.getId().equals(roomId))
        {
          return true;
        }
      }
    }

    return false;
  }


}
