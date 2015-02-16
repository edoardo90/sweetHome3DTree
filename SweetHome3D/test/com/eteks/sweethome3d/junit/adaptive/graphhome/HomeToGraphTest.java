package com.eteks.sweethome3d.junit.adaptive.graphhome;

import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingRoomNode;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.adaptive.security.extractingobjs.ConfigLoader;
import com.eteks.sweethome3d.adaptive.security.extractingobjs.HomeSecurityExtractor;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.adaptive.BasicTest;
import com.eteks.sweethome3d.junit.adaptive.ConfigFileEvilTest;
import com.eteks.sweethome3d.junit.adaptive.ControllerTest;
import com.eteks.sweethome3d.model.CatalogPieceOfFurniture;
import com.eteks.sweethome3d.model.FurnitureCategory;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.RoomGeoSmart;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.model.Wall;
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
  
  public void testMove()
  {
        
    HomeSecurityExtractor hse = new HomeSecurityExtractor(home, preferences);
    try 
    {
        BuildingSecurityGraph segraph =  hse.getGraph();
//        System.out.println(segraph);
//        System.out.println("MOOOVEEEEE");
        segraph.moveObject("washing", "kitchen");
//        System.out.println(segraph);
        assertTrue("the object has been moved", containsDeep(segraph, "kitchen", "washing"));
    }
    catch (Exception ex)   {     ex.printStackTrace();   }
    
  }
  
  public void testAdd() throws Exception
  {
    
    HomePieceOfFurniture hopf = super.getHomePiece(preferences, 1, 2, "wash", 100, 100);
    home.addPieceOfFurniture(hopf);
    
    HomeSecurityExtractor hse = new HomeSecurityExtractor(home, preferences);
    BuildingSecurityGraph segraph =  hse.getGraph();
    
    System.out.println(segraph);
    
    assertTrue("there should be wash in cubby room",
           this.containsDeep(segraph, this.cubbyRoom.getId(), "wash"));
    
    segraph.addNewObject("light1", BuildingObjectType.LIGHT, "kitchen", 
         new Vector3D(300, 400, 0));
    
    
    assertTrue("light1 should be in kitchen", 
        this.containsDeep(segraph, this.kitchen.getId(), "light1"));
    
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
    
  }
  
  
  
  private void x()
  {
    ConfigFileEvilTest cfg = ConfigFileEvilTest.getInstance(preferences);
    String nameOfCCTV =  cfg.getSweetHomeNameForType(BuildingObjectType.CCTV);
    System.out.println("cctv : " + nameOfCCTV);   //Doccia
    String objectName = "Doccia";
    BuildingObjectType typeOfObject = cfg.getTypeForSweetHomeName(objectName);
    System.out.println(" type  :" + typeOfObject);

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
        System.out.println(segraph);
        segraph.moveObject("washing", "kitchen");
        System.out.println("\n\n =============MOVE ===========\n\n");
        System.out.println(segraph);
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
        if(obj.getId().equals(objectId))
        {
          return true;
        }
      }
    }
    
    return false;
  }
  
  
}
