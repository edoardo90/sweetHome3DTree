package com.eteks.sweethome3d.junit.adaptive.graphhome;

import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingRoomNode;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.ifcSecurity.HomeSecurityExtractor;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.adaptive.BasicTest;
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

  private RoomGeoSmart        squareRoom;
  private RoomGeoSmart        rectangularRoom;
  private RoomGeoSmart        rectangularRoomShifted;
  private RoomGeoSmart        elRoom1;
  private RoomGeoSmart        elRoom2;
  private RoomGeoSmart        wallBetweenL;
  private RoomGeoSmart        squareBelowL1;
  private RoomGeoSmart         wallBetweenL1Square;
  private RoomGeoSmart wallAside;
  private RoomGeoSmart rectangularRoomShiftedDown;
  protected Wall w1;
  private Wall w2;
  private Wall w3;
  
  @Override
  public void setUp()
  {
    this.squareRoom = getSquareRoom();
    this.rectangularRoomShifted = getRectangularShortRoom(240, 0);
    
    w1 = new Wall(220, 0, 220, 200, 30, home.getWallHeight());
    w2 = new Wall(0, 0, 1000, 0, 30, home.getWallHeight());
    w3 = new Wall(0, 220, 1000, 220, 30, home.getWallHeight());
    
    this.squareRoom.setName("kitchen");
    this.rectangularRoomShifted.setName("living room");
    
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
    
    home.addRoom(this.squareRoom);
    home.addRoom(this.rectangularRoomShifted);
    home.addWall(w1);
    home.addWall(w2);
    home.addWall(w3);
    
    FurnitureCategory cat =  preferences.getFurnitureCatalog().getCategories().get(1);
    List<CatalogPieceOfFurniture> furnitures = cat.getFurniture();
    
    HomePieceOfFurniture hopf = new HomePieceOfFurniture(furnitures.get(4));
    
    hopf.setX(400);
    hopf.setY(100);
    home.addPieceOfFurniture(hopf);
    
    hopf.setId("washing");
    this.squareRoom.setId("kitchen");
    this.rectangularRoomShifted.setId("living room");
    
    HomeSecurityExtractor hse = new HomeSecurityExtractor(home, preferences);
    try 
    {
        BuildingSecurityGraph segraph =  hse.getGraph();
    
        System.out.println(segraph);
      
        segraph.moveObject("washing", "kitchen");
        System.out.println("\n\n =============MOVE ===========\n\n");
        
        System.out.println(segraph);
    
    }
    catch (Exception ex) 
    {
      ex.printStackTrace();
    }
  }
  
  private boolean containsDeep(BuildingSecurityGraph segraph, String roomId, String objectId)
  {
    
    List<BuildingRoomNode> roomNodes = segraph.getRoomNodeList();
    
    for(BuildingRoomNode roomNode : roomNodes)
    {
      List<BuildingObjectContained> contained = roomNode.getObjectsInside();
      for(BuildingObjectContained obj : contained)
      {
        if(obj.equals(objectId))
        {
          return true;
        }
      }
    }
    
    return false;
  }
  
  
}
