package com.eteks.sweethome3d.adaptive.security.buildingGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.IdObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.IdRoom;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.model.Wall;

/**
 * Data Structure used to represent the graph associated to the building
 * @author Edoardo Pasi
 */
public class BuildingSecurityGraph {
    
  private List<BuildingLinkEdge> linkEdgeList = new ArrayList<BuildingLinkEdge>();
  private List<BuildingRoomNode> roomNodeList = new ArrayList<BuildingRoomNode>();
  private List<Wall> notLinkingWalls = new ArrayList<Wall>();
  private List<CyberLinkEdge> cyberLinkEdgeList = new ArrayList<CyberLinkEdge>();
  
  private Map<IdRoom, BuildingRoomNode>  buildingRooms = new HashMap<IdRoom, BuildingRoomNode>();
  private Map<IdObject, BuildingRoomNode>  objectsRoomLocation = new HashMap<IdObject, BuildingRoomNode>();
  private Map<IdObject, BuildingObjectContained> objectsContained = new HashMap<IdObject, BuildingObjectContained>();
  
  private static BuildingSecurityGraph instance = null;
  
  public static BuildingSecurityGraph getInstance()
  {
    if (BuildingSecurityGraph.instance == null)
    {
      BuildingSecurityGraph.instance = new BuildingSecurityGraph();
      return BuildingSecurityGraph.instance;
    }
    else
    {
      return BuildingSecurityGraph.instance;
    }
  }
  
  private BuildingSecurityGraph()
  {
    
  }
  
  public List<BuildingLinkEdge> getLinkEdgeList() {
    
    Collections.sort(this.linkEdgeList, new Comparator<BuildingLinkEdge>() {

      public int compare(BuildingLinkEdge o1, BuildingLinkEdge o2) {
          return o1.toString().compareTo(o2.toString());
      }
    });
    
    return linkEdgeList;
  }
  public void setLinkEdgeList(List<BuildingLinkEdge> linkEdgeList) {
    this.linkEdgeList = linkEdgeList;
  }
  public List<BuildingRoomNode> getRoomNodeList() {
    return roomNodeList;
  }
  public void setRoomNodeList(List<BuildingRoomNode> roomNodeList) {
    this.roomNodeList = roomNodeList;
  }
  public List<Wall> getNotLinkingWalls() {
    
    
    return notLinkingWalls;
  }
  public void setNotLinkingWalls(List<Wall> notLinkingWalls) {
    this.notLinkingWalls = notLinkingWalls;
  }

  public List<CyberLinkEdge> getCyberLinkEdgeList() {
    return cyberLinkEdgeList;
  }

  public void setCyberLinkEdgeList(List<CyberLinkEdge> cyberLinkEdgeList) {
    this.cyberLinkEdgeList = cyberLinkEdgeList;
  }
  
  
  public void clearAll()
  {
    
    this.linkEdgeList.clear();
    this.roomNodeList.clear();
    this.notLinkingWalls.clear();
    this.cyberLinkEdgeList.clear();
    
  }
  
  /**
   * 
   * @param idObject:  the object to move
   * @param idRoom:  the room in which move it
   * Updates the map of rooms and objects
   * Updates the room object
   */
  public void moveObject(String idObject, String idRoomDestination)
  {
    BuildingRoomNode broomDestination = this.buildingRooms.get(new IdRoom(idRoomDestination));
    BuildingRoomNode  broomSource = this.objectsRoomLocation.get(new IdObject(idObject));
    BuildingObjectContained objectCont = this.objectsContained.get(new IdObject(idObject));
    
    broomDestination.addObjectContained(objectCont);
    broomSource.removeObject(objectCont);
  
  }
  
  public void putObjectCont(IdObject idObj, BuildingObjectContained cont)
  {
    this.objectsContained.put(idObj, cont);
  }
  
  public void putObjectRoom(IdObject idObj, BuildingRoomNode room)
  {
    this.objectsRoomLocation.put(idObj, room);
  }
  
  public void putBuildingRoom(IdRoom idRoom, BuildingRoomNode room)
  {
    this.buildingRooms.put(idRoom, room);
  }
  
  
  @Override
  public String toString()
  {
    String s = "";
    for(BuildingRoomNode roomNode : this.roomNodeList )
    {
      s = s + "\nROOM: \n" + roomNode;
      for(BuildingObjectContained bojc : roomNode.getObjectsInside())
      {
        s = s + "\n\t" + bojc;
      }
      s = s + "\n\n";
    }
    
    s = s + " LINKS : \n";
    if(this.getLinkEdgeList().isEmpty())
    {
      System.out.println("\t [ ] ");
    }
    for(BuildingLinkEdge link : this.getLinkEdgeList())
    {
      s = s + link + "\n\n";
    }
    
    
    return s;
    
  }
  
  
  
}
