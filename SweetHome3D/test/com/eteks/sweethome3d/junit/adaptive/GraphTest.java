package com.eteks.sweethome3d.junit.adaptive;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildinLinkWallWithDoor;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingLinkEdge;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingLinkWall;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingRoomNode;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.DoorObject;
import com.eteks.sweethome3d.adaptive.security.extractingobjs.GraphClean;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.adaptive.FurnitureAddTest.ControllerTest;
import com.eteks.sweethome3d.model.CatalogPieceOfFurniture;
import com.eteks.sweethome3d.model.FurnitureCategory;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.Home.UpdateBehaviour;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.PieceOfFurniture;
import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.model.Wall;
import com.eteks.sweethome3d.swing.SwingViewFactory;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

public class GraphTest extends BasicTest {



  public  static void main(String [] args) {
    ViewFactory viewFactory = new SwingViewFactory();
    UserPreferences preferences ;
    
    
    preferences = new DefaultUserPreferences();
    preferences.setUnit(LengthUnit.METER);
    
    Home home = new Home();
    ControllerTest t = new ControllerTest(home, preferences, viewFactory);

    GraphTest f = new GraphTest();
    f.doStuffInsideMain(home, preferences);

  }

  @Override
  public void doStuffInsideMain(Home home, UserPreferences preferences) {
    try
    {
      super.setUp();
      BuildingSecurityGraph  securityGraph = 
          super.openIfcAndReadIt(home, this.preferences, "5 rooms with objects.ifc");
      
      GraphClean gc = new GraphClean(securityGraph);
      gc.populateGraph();
      System.out.println("woo");
      gc.show();
      
      System.out.println("sec:\n"  + securityGraph);
      
      this.displayGraph(home, securityGraph, this.preferences);
      //HomePieceOfFurniture hopf = this.getHomePieceOfForniture(preferences, BuildingObjectType.CCTV, new Vector3D());
      //home.addPieceOfFurniture(hopf);
      
    } 
    catch (Exception ex) {

      ex.printStackTrace();
    }

  }
  
  private  void displayGraph(Home home, BuildingSecurityGraph securityGraph, UserPreferences preferences) {

    List<BuildingRoomNode> roomsInBuilding = securityGraph.getRoomNodeList();
    for(BuildingRoomNode rib : roomsInBuilding)
    {
      Room r = rib.getRoomSmart();
      List<BuildingObjectContained> objectsInside = rib.getObjectsInside();
      for(BuildingObjectContained objectContained : objectsInside )
      {
         Vector3D objPosition =  objectContained.getPosition();
         HomePieceOfFurniture hopf = this.getHomePieceOfForniture(preferences, objectContained.getType(), new Vector3D());
         hopf.setX((float)objPosition.first);
         hopf.setY((float)objPosition.second);
         hopf.setElevation(0);
         hopf.setAngle(0);
         hopf.setId(objectContained.getId());
         
         home.addPieceOfFurniture(hopf, UpdateBehaviour.DONT_UPDATE_GRAPH);
         
      }
    
      home.addRoom(r);  
    }
    
    List<BuildingLinkEdge> linksInBuilding = securityGraph.getLinkEdgeList();
    
    List<WallProperties> lstAddedWalls = new ArrayList<GraphTest.WallProperties>();
    
    for(BuildingLinkEdge link : linksInBuilding)
    {
         if(link instanceof BuildingLinkWall)
         {
           BuildingLinkWall linkWithWall = (BuildingLinkWall)link;
           Wall wall = linkWithWall.getWall();
           WallProperties wp = new WallProperties(wall);
           
           if(! lstAddedWalls.contains(wp))
           { 
                lstAddedWalls.add(wp);
                home.addWall(wall);
           }
           
           if(linkWithWall instanceof BuildinLinkWallWithDoor)
           {
             BuildinLinkWallWithDoor linkDoor = (BuildinLinkWallWithDoor) linkWithWall;
             DoorObject door =  linkDoor.getDoor();
             HomePieceOfFurniture doorHopf = this.getDoor();
             Vector3D position = door.getPosition();
             float angle = door.getAngle();
             doorHopf.setX((float)position.first);
             doorHopf.setY((float)position.second);
             doorHopf.setAngle(angle);
             home.addPieceOfFurniture(doorHopf);
           }
         }
    }
    
    List<Wall> otherWalls = securityGraph.getNotLinkingWalls();
    for(Wall wall : otherWalls)
    {
      home.addWall(wall);
    }
    
  }

  private HomePieceOfFurniture getDoor()
  {
    HomePieceOfFurniture door = null;
    Locale.setDefault(Locale.US);
    // Read default user preferences
    UserPreferences preferences = new DefaultUserPreferences();
    List<FurnitureCategory> categories= preferences.getFurnitureCatalog().getCategories();
    for(FurnitureCategory category : categories )
    {
      List<CatalogPieceOfFurniture> catalogObjs = category.getFurniture();
      for(PieceOfFurniture piece : catalogObjs)
      {
                  String pieceName = piece.getName();
        if(pieceName.equals("Door"))
        {
          door = new HomePieceOfFurniture(piece);
          
        }
      }
    }    

    
    return door;
  }
  
  private class WallProperties
  {
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      
      result = prime * result + Float.floatToIntBits(this.height);
      result = prime * result + Float.floatToIntBits(this.thickness);
      result = prime * result + Float.floatToIntBits(this.xend);
      result = prime * result + Float.floatToIntBits(this.xstart);
      result = prime * result + Float.floatToIntBits(this.yend);
      result = prime * result + Float.floatToIntBits(this.ystart);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      WallProperties other = (WallProperties)obj;
      if (Float.floatToIntBits(this.height) != Float.floatToIntBits(other.height))
        return false;
      if (Float.floatToIntBits(this.thickness) != Float.floatToIntBits(other.thickness))
        return false;
      if (Float.floatToIntBits(this.xend) != Float.floatToIntBits(other.xend))
        return false;
      if (Float.floatToIntBits(this.xstart) != Float.floatToIntBits(other.xstart))
        return false;
      if (Float.floatToIntBits(this.yend) != Float.floatToIntBits(other.yend))
        return false;
      if (Float.floatToIntBits(this.ystart) != Float.floatToIntBits(other.ystart))
        return false;
      return true;
    }

    float xstart, ystart, xend, yend, thickness, height;
    
    @SuppressWarnings("unused")
    public WallProperties(final float xstart, final float ystart, final float xend, final float yend, final float thickness, final float height)
    {
      this.xstart = xstart;
      this.xend = xend;
      this.ystart = ystart;
      this.yend = yend;
      this.thickness = thickness;
      this.height = height;
    }
    
    public WallProperties(Wall w)
    {
     
      this.xstart = w.getXStart();
      this.xend = w.getXEnd();
      this.ystart = w.getYStart();
      this.yend = w.getYEnd();
      this.thickness = w.getThickness();
      this.height = w.getHeight();
      
    }

    
    
  }


  
  
  
  

}
