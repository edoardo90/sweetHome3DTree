package com.eteks.sweethome3d.adaptive.security.buildingGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.IdObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.IdRoom;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.WrapperRect;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.adaptive.security.extractingobjs.ConfigLoader;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.Wall;
import com.sun.xml.internal.bind.v2.model.core.ID;

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
  
  private List<WrapperRect>  spaceAreasOfRooms = new ArrayList<WrapperRect>();
  /** now inefficient in the future could be  a btree  TODO: btree **/
  
  
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
    for(BuildingRoomNode brn : roomNodeList)
    {
      WrapperRect r = brn.getWrappedRect();
      r.setRoomId(brn.getId());
      if( ! this.spaceAreasOfRooms.contains(r))
      { 
        this.spaceAreasOfRooms.add(r);
      }
    }
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
  
  public void addSpaceAreaOfRoom(WrapperRect rectRoom)
  {
    this.spaceAreasOfRooms.add(rectRoom);
  }
  
  public String getRoomId(Vector3D position)
  {
    /** this should search in btree **/
    for(WrapperRect rect : this.spaceAreasOfRooms)
    {
      if (rect.equals(position))
      {
        String roomId = rect.getRoomId();
        return roomId;
      }
    }
    return null;
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
    //TODO: update the position of the object
    
    IdRoom IDDEST = new IdRoom(idRoomDestination);
    IdObject IDOBJ = new IdObject(idObject);
    
    BuildingRoomNode broomDestination = this.getBuildingRoomFromRoom(IDDEST);
    BuildingRoomNode  broomSource = this.getBuildingRoomFromObj(IDOBJ);
    BuildingObjectContained objectCont = this.getObjectContainedFromObj(IDOBJ);
    
    if(broomDestination == null || broomSource == null || objectCont == null)
    {
      throw new IllegalStateException("graph probably not updated");
      
    }
    
    if(broomDestination.getId().equals(broomSource.getId()))
    {
      return;
    }
    
    broomDestination.addObjectContained(objectCont);
    broomSource.removeObject(objectCont);
    
    this.objectsRoomLocation.remove(IDOBJ);
    this.objectsRoomLocation.put(IDOBJ, broomDestination);
    
    System.out.println("CHANGED!!" + this);
    
  }
  
  private BuildingObjectContained getObjectContainedFromObj(IdObject IDOBJ)
  {
    BuildingObjectContained boc = this.objectsContained.get(IDOBJ);
    
    return boc;
  }
  
  private BuildingRoomNode getBuildingRoomFromRoom(IdRoom IDROOM)
  {
    BuildingRoomNode brn = this.buildingRooms.get(IDROOM);
    return brn;
  }
  private BuildingRoomNode getBuildingRoomFromObj(IdObject IDOBJ)
  {
    BuildingRoomNode brn = this.objectsRoomLocation.get(IDOBJ);
    return brn;
  }
  
  
  public void addNewObject(String idObject, BuildingObjectType type, String idRoomDestination, Vector3D position )
  {
    
    BuildingRoomNode broomDestination = this.getBuildingRoomFromRoom(new IdRoom(idRoomDestination));
    if(broomDestination == null)
    {
      throw new IllegalStateException("graph not updated");
    }
    BuildingObjectContained objectCont = type.getBuildingObjectOfType(position);
    objectCont.setId(idObject);
    broomDestination.addObjectContained(objectCont);
    
    IdObject ID = new IdObject(idObject);
    this.objectsRoomLocation.put(ID , broomDestination);
    this.objectsContained.put(ID, objectCont);
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

  public void moveObject(String idObject, Vector3D position) {
    String roomId = this.getRoomId(position);
    moveObject(idObject, roomId);
    
  }

  public void removeObject(String idObject) {
    IdObject IDOBJ = new IdObject(idObject);
    BuildingRoomNode containingRoom = this.getBuildingRoomFromObj(IDOBJ);
    BuildingObjectContained contained = this.getObjectContainedFromObj(IDOBJ);
    
    if(containingRoom == null || contained == null)
    {
      throw new IllegalStateException("graph not updated");
    }
    
    //from room
    containingRoom.removeObject(contained);
    //from maps
    this.objectsContained.remove(IDOBJ);
    this.objectsRoomLocation.remove(IDOBJ);
    
  }

  public void moveObject(HomePieceOfFurniture homePieceOfFurniture, Vector3D position) {
      this.moveObject(homePieceOfFurniture.getId(), position);
  }

  public void addNewObject(String idObject, BuildingObjectType type, Vector3D position) {
    String idRoomDestination;
    BuildingRoomNode buildRoom = this.getBuildingRoomFromObj(new IdObject(idObject));
    if(buildRoom == null)
    {
      throw new IllegalStateException("graph not updated");
    }
    idRoomDestination = buildRoom.getId();
    this.addNewObject(idObject, type, idRoomDestination, position);
    
  }
  
  
  
}
