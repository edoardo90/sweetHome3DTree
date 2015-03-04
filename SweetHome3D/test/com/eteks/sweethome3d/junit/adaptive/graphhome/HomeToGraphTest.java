package com.eteks.sweethome3d.junit.adaptive.graphhome;

import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingRoomNode;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.CyberLinkEdge;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.adaptive.security.extractingobjs.HomeSecurityExtractor;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.HomeExtractorEvil;
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

    HomeSecurityExtractor hse = new HomeExtractorEvil(home, preferences);

    BuildingSecurityGraph segraph =  hse.getGraph();

    segraph.moveObject(this.pc.getId(), this.kitchen.getId());

    assertTrue("the object has been moved",
        containsDeep(segraph, this.kitchen.getId(), this.pc.getId()));

    assertFalse("the object should be only in kitchen",
        containsDeep(segraph, this.cubbyRoom.getId(), this.pc.getId()));

    assertFalse("the object should be only in kitchen",
        containsDeep(segraph, this.diningRoom.getId(), this.pc.getId()));

    assertFalse("the object should be only in kitchen",
        containsDeep(segraph, this.livingRoom.getId(), this.pc.getId()));

  }
  
  public void testCyberLink() throws Exception
  {
    
    HomeExtractorEvil hee = new HomeExtractorEvil(home, preferences);
    BuildingSecurityGraph segraph =  hee.getGraph();
    System.out.println(segraph);
    
    boolean illegalArg = false;
    try
    {
      segraph.addCyberLink(this.cctv.getId(), this.pc.getId());
    }
    catch(IllegalArgumentException iae)
    {
      illegalArg = true;
    }
    assertTrue("can't link CCTV and PC because CCTV can't accept connections", illegalArg);

    
    segraph.addCyberLink(this.printer.getId(), this.pc.getId());
    
    assertTrue("printer and pc should be linked",
                    this.isThereLink(segraph, printer.getId(),  pc.getId()));
    
    segraph.removeCyberLink(this.printer.getId(), this.pc.getId());
    
    assertFalse("printer and pc should not be linked anymore",
        this.isThereLink(segraph, printer.getId(),  pc.getId()));
    
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

    segraph.addNewObject("light1", BuildingObjectType.LIGHT, "light", "light", "kitchen", 
         new Vector3D(300, 400, 0));


    assertTrue("light1 should be in kitchen", 
        this.containsDeep(segraph, this.kitchen.getId(), "light1"));

    segraph.moveObject("light1", new Vector3D(700, 150, 0));

    assertTrue("light1 has been moved in livingRoom", 
        this.containsDeep(segraph, this.livingRoom.getId(), "light1"));



  }

  public void testMatchCoords() throws Exception
  {
    HomeSecurityExtractor hse = new HomeExtractorEvil(home, preferences);
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
    HomeSecurityExtractor hse = new HomeExtractorEvil(home, preferences);
    try 
    {  
      BuildingSecurityGraph segraph =  hse.getGraph();
      segraph.moveObject(this.pc.getId(), this.kitchen.getId());
      
      
    
    }
    catch (Exception ex)  {     ex.printStackTrace();  }

  }
  
  private boolean isThereLink(BuildingSecurityGraph segraph, String id1, String id2)
  {
    List<CyberLinkEdge> cyberLinks = segraph.getCyberLinkEdgeList();
    for(CyberLinkEdge cle : cyberLinks)
    {
      boolean eq = cle.equals(new CyberLinkEdge(id1, id2));
      if(eq)
        return true;
      
    }
    
    return false;
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
