package com.eteks.sweethome3d.adaptive.security.buildingGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.IdObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.IdRoom;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.WrapperRect;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
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
  
  private List<WrapperRect>  spaceAreasOfRooms = new ArrayList<WrapperRect>();
  /** now inefficient in the future could be  a btree  TODO: btree **/
  
  private BTree<WrapperRect, String> spaceAreasTT = new BTree<WrapperRect, String>();
  
  
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
        this.spaceAreasTT.put(r, brn.getId());
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
  
  @SuppressWarnings("unused") // see the todo
  public String getRoomId(Vector3D position)
  {
    String roomId = null;
    /** this should search in btree **/
    for(WrapperRect rect : this.spaceAreasOfRooms)
    {
      if (rect.equals(position))
      {
         roomId = rect.getRoomId();
      }
    }
    
    String roomId_btree; 
    roomId_btree = this.spaceAreasTT.get(new WrapperRect(position));
    
    if(false) //TODO: try more and maybe keep just the btree
            System.out.println(" room id old way : " + roomId + 
                               "\n room id new way :  "  + roomId_btree);
    
      
    return roomId;
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
    
    System.out.println("MOVE - CHANGED!!" + this);
    
  }
  
  public BuildingObjectContained getObjectContainedFromObj(IdObject IDOBJ)
  {
    BuildingObjectContained boc = this.objectsContained.get(IDOBJ);
    
    return boc;
  }
  
  public BuildingRoomNode getBuildingRoomFromRoom(IdRoom IDROOM)
  {
    BuildingRoomNode brn = this.buildingRooms.get(IDROOM);
    return brn;
  }
  public BuildingRoomNode getBuildingRoomFromObj(IdObject IDOBJ)
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
    //TODO: pass name too
    broomDestination.addObjectContained(objectCont);
    
    IdObject ID = new IdObject(idObject);
    this.objectsRoomLocation.put(ID , broomDestination);
    this.objectsContained.put(ID, objectCont);
    
    System.out.println("ADD - CHANGED!!" + this);
    
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
  
  public String mapsToString()
  {
    
    String  s = "OBJECTS ID -> OBJ CONT: \n";
    
    for(Entry<IdObject, BuildingObjectContained> entry : this.objectsContained.entrySet())
    {
       s = s + "\t" +  entry.getKey() + "\n";
       s = s + "\t" +  entry.getValue().typeString()  + "\n\n";
    }
    
    s = s + "\n  ROOMS ID -> BUILDING ROOM : \n";
    for(Entry<IdRoom, BuildingRoomNode> entry : this.buildingRooms.entrySet())
    {
      s = s + "\t" + entry.getKey() + "\n";
      s = s + "\t" + entry.getValue().getName() + "\n\n";
    }
        
    s =  s + "\n OBJECT ID  ->  BUILDING ROOM \n";
    for(Entry<IdObject, BuildingRoomNode> entry :  this.objectsRoomLocation.entrySet())
    {
      s = s + "\t" + entry.getKey() + "\n";
      s = s + "\t" + entry.getValue().getName() + "\n\n";
    }
    
    s = s + "\n\n BTREE ";
    
    s = s + this.spaceAreasTT;
    
    return s;
    
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
        s = s + "\n\t" + bojc ;
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
    
    System.out.println("DELETE - CHANGED!" + this);
    
  }

  public void moveObject(HomePieceOfFurniture homePieceOfFurniture, Vector3D position) {
      this.moveObject(homePieceOfFurniture.getId(), position);
  }

  public void addNewObject(String idObject, BuildingObjectType type, Vector3D position) {
    String idRoomDestination;
    idRoomDestination = this.getRoomId(position);
    if(idRoomDestination == null)
    {
      throw new IllegalStateException("getRoomId(position) failed  - as if it there was no room there");
    }
    this.addNewObject(idObject, type, idRoomDestination, position);
    
  }

  public void addCyberLink(String id1, String id2) {
      BuildingObjectContained bo1 = this.getObjectContainedFromObj(new IdObject(id1));
      BuildingObjectContained bo2 = this.getObjectContainedFromObj(new IdObject(id2));
      
      if(bo1 == null || bo2 == null)
      {
        throw new IllegalStateException("Id not found maybe the graph is not updated");
      }
      
      BuildingObjectType t1 = bo1.getType();
      BuildingObjectType t2 = bo2.getType();
      
      if(t1.canConnect() && t2.canConnect() )  
      {
        this.cyberLinkEdgeList.add( new CyberLinkEdge(id1, id2));
      }
      else
      {
        throw new IllegalArgumentException("these 2 objects can't be linked");
      }
        
      
  }

  public void removeCyberLink(String id1, String id2) {
      this.cyberLinkEdgeList.remove(new CyberLinkEdge(id1, id2));
  }

  public BuildingObjectContained getObjectContainedFromHOPF(HomePieceOfFurniture hopf) {
    IdObject id =  new IdObject(hopf.getId());
    
    BuildingObjectContained boo = this.getObjectContainedFromObj(id);
    if(boo == null)
    {
      throw new IllegalStateException("object not found");
    }
    return boo;
  }

  public List<String> getListStrContainedObjects() {
    List<String> lstOjbects = new ArrayList<String>();
    for(BuildingObjectContained boc : this.objectsContained.values())
    {
       lstOjbects.add(boc.getStringRepresent());
    }
    return lstOjbects;
  }
  
  
  
}
