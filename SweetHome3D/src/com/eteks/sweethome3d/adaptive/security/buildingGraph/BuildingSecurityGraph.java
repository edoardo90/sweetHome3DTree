package com.eteks.sweethome3d.adaptive.security.buildingGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.policy.ABACPolicy;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.IdObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.IdRoom;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.WrapperRect;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.ActorObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.GeneralFileHolder;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.GeneralMaterialObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.PCObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.PrinterObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.attributes.BuildingObjectAttribute;
import com.eteks.sweethome3d.adaptive.security.extractingobjs.ConfigLoader;
import com.eteks.sweethome3d.adaptive.security.extractingobjs.ObjectAbility;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.model.RoomGeoSmart;
import com.eteks.sweethome3d.model.Wall;

/**
 * Data Structure used to represent the graph associated to the building
 * @author Edoardo Pasi
 */
public class BuildingSecurityGraph {
    
  /**
   * The BuildingSecurityGraph is singleton
   * @return
   */
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
  /**
   * 
   * @return <pre> List of BuildingLinkEdge is 
   * a list of {@link BuildingLinkEdge}, each element of the
   * list represent a connection between two rooms.
   * A connection can be given by a <b> wall that does not contain any door </b>
   * and then it will be represented as
   * {@link BuildingLinkWall},
   * otherwise a connection can be given by a <b> wall that contains a door </b>
   * and then it will be represented as
   * {@link BuildinLinkWallWithDoor}
   * </pre>
   * 
   */
  public List<BuildingLinkEdge> getLinkEdgeList() {
    
    Collections.sort(this.linkEdgeList, new Comparator<BuildingLinkEdge>() {

      public int compare(BuildingLinkEdge o1, BuildingLinkEdge o2) {
          return o1.toString().compareTo(o2.toString());
      }
    });
    
    return linkEdgeList;
  }
  /**
   * Setter for list. 
   * <pre> List of BuildingLinkEdge is 
   * a list of {@link BuildingLinkEdge}, each element of the
   * list represent a connection between two rooms.
   * A connection can be given by a <b> wall that does not contain any door </b>
   * and then it will be represented as
   * {@link BuildingLinkWall},
   * otherwise a connection can be given by a <b> wall that contains a door </b>
   * and then it will be represented as
   * {@link BuildinLinkWallWithDoor}
   * </pre>
   * @param linkEdgeList
   */
  public void setLinkEdgeList(List<BuildingLinkEdge> linkEdgeList) {
    this.linkEdgeList = linkEdgeList;
  }
  
  /**
   * 
   * @return <pre> a list of {@link BuildingRoomNode}, each of them represent
   * a room inside the building.
   * A BuildindRoomNode contains:
   *    - a List of {@link BuildingObjectContained} :  the objects contained in the room
   *    - a {@link RoomGeoSmart} : a decorated version of the {@link Room}  SW3D object
   * Given a BuildingRoomNode it is possible to get the RoomGeosmart contained,
   * it is possible to get a list of objects contained
   * it is possible to get a {@link WrapperRect} of the room, that is basically a bounding box
   * of the Room, but just more suitable for detecting containment of points   
   * 
   * </pre>
   */
  
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
  
  
  /**
   * 
   * @return the list of all {@link Wall} that are not used to link rooms,
   * that is the list of all walls that are used for representing the building
   * but that are not used in the Graph
   */
  public List<Wall> getNotLinkingWalls() {
    
    
    return notLinkingWalls;
  }
  public void setNotLinkingWalls(List<Wall> notLinkingWalls) {
    this.notLinkingWalls = notLinkingWalls;
  }
  
  /**
   * 
   * @return <pre> a list of {@link CyberLinkEdge}.
   * Each element of a list represent a virtual connection between 2 objects
   * or between 2 agents.
   * For instance a CyberLink can describe the connection between an Agent and 
   * a Pc, or between a Pc and a Printer
   * See: {@link PCObject}, {@link PrinterObject}, {@link ActorObject} 
   * </pre>
   */
  public List<CyberLinkEdge> getCyberLinkEdgeList() {
    return cyberLinkEdgeList;
  }
  /**
   * Setter
   * @param cyberLinkEdgeList
   */
  public void setCyberLinkEdgeList(List<CyberLinkEdge> cyberLinkEdgeList) {
    this.cyberLinkEdgeList = cyberLinkEdgeList;
  }
  
  /**
   * <pre>
   * When the user moves and object from a position to another, inside the
   * coordinate space of SW3D, it is necessary to update the Graph.
   * 
   * For instance the user can move an HVAC from (200, 300) to (6000, 300)
   * doing so, potentially he is changing the containment relation for that particular
   * HVAC because the room containing it can be potentially will be different.
   * 
   * For this reason it necessary <b> translating a coordinate (x,y) into a roomId </b>
   * that in turn will be translated into a BuildingRoomNode
   * </pre> 
   * @param position, feel free to set z = 0, in any case the z attribute of {@link Vector3D} 
   * will be ignored
   * @return the Id of the room that contains the position passed as parameter
   */
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
  
  /**
   *<pre>
   *  Clear all the lists.
   * 
     this.linkEdgeList.clear();
     this.roomNodeList.clear();
     this.notLinkingWalls.clear();
     this.cyberLinkEdgeList.clear();

     </pre>
   */
  public void clearAll()
  {
    
    this.linkEdgeList.clear();
    this.roomNodeList.clear();
    this.notLinkingWalls.clear();
    this.cyberLinkEdgeList.clear();
    
  }
  
  /**
   * This method is used to update the Graph when the user moves an object
   * from a room to another
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
  
  /**
   * <pre>
   * The father of the object is the object that contains it, or null
   * Mater semper certa est ... pater numquam ...
   * </pre>
   * @param IDOBJECT
   * @return the {@link BuildingObjectContained} representing the container or null
   */
  public BuildingObjectContained getFatherOfObject(IdObject IDOBJECT)
  {
    return this.objectsFather.get(IDOBJECT);
  }
  /**
   * 
   * @param IDOBJCT
   * @param father: the container of the object whose id is wrapped in {@link IdObject}
   */
  public void putFather(IdObject IDOBJCT, BuildingObjectContained father)
  {
    this.objectsFather.put(IDOBJCT, father);
  }
  
  public void addNewObject(String idObject,
                           BuildingObjectType type, 
                           String pieceName,
                           String pieceOriginalName,
                           String idRoomDestination,
                           Vector3D position )
  {
    
    BuildingRoomNode broomDestination = this.getBuildingRoomFromRoom(new IdRoom(idRoomDestination));
    if(broomDestination == null)
    {
      throw new IllegalStateException("graph not updated");
    }
    BuildingObjectContained objectCont = type.getBuildingObjectOfType(position);
    
    objectCont.setId(idObject);
    objectCont.setName(pieceName);
    objectCont.setOriginalName(pieceOriginalName);
    
    this.setAbilitiesAndAttributes(objectCont);
    if(type == BuildingObjectType.UNKNOWN_OBJECT)
    {
      if( objectCont.getAbilities().contains(ObjectAbility.STORE_FILES))
      {
        objectCont = new GeneralFileHolder(position);
      }
      else
      {
        objectCont = new GeneralMaterialObject(position);
      }
      

      objectCont.setId(idObject);
      objectCont.setName(pieceName);
      objectCont.setOriginalName(pieceOriginalName);
      this.setAbilitiesAndAttributes(objectCont);

      
    }
    

    
    broomDestination.addObjectContained(objectCont);
    
    IdObject ID = new IdObject(idObject);
    this.objectsRoomLocation.put(ID , broomDestination);
    this.objectsContained.put(ID, objectCont);
    
    System.out.println("ADD - CHANGED!!" + this);
    
  }
  
  private void setAbilitiesAndAttributes(BuildingObjectContained objectCont) {
    this.setAttributes(objectCont);
    this.setAbilities (objectCont);
  }

  private void setAbilities(BuildingObjectContained objectCont) {
     
     ConfigLoader cfg = ConfigLoader.getInstance();
     String originalName = objectCont.getOriginalName();
     if(originalName == null)
         throw new IllegalStateException("original name is null!" + "objectCont : " + objectCont);
     Set<ObjectAbility> abilities = cfg.getObjectAbilities(originalName);
     if(abilities == null)
       return;
     objectCont.setAbilities(abilities);
    
  }

  private void setAttributes(BuildingObjectContained objectCont) {
      
    ConfigLoader cfg = ConfigLoader.getInstance();
    String originalName = objectCont.getOriginalName();
    Set<BuildingObjectAttribute> attrs = cfg.getPossibleAttributesForObject(originalName);
    if(attrs == null)
      return;
    objectCont.addAllAttributes(attrs);
    
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
    
    s = s + "\n OBJECT ID -> OBJECT ID FATHER \n";
    for(Entry<IdObject, BuildingObjectContained> entry : this.objectsFather.entrySet())
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
    
    s = s + this.mapsToString();
    
    return s;
    
  }

  public void moveObject(String idObject, Vector3D position) {
    String roomId = this.getRoomId(position);
    moveObject(idObject, roomId);
    
  }

  public void removeIDObjectFromIDOBJMap(IdObject id)
  {
    this.objectsContained.remove(id);
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

  public void addNewObject(String idObject, BuildingObjectType type,
                           String pieceName, String pieceOriginalName,
                           Vector3D position) {
    String idRoomDestination;
    idRoomDestination = this.getRoomId(position);
    if(idRoomDestination == null)
    {
      throw new IllegalStateException("getRoomId(position) failed  - as if it there was no room there");
    }
    this.addNewObject(idObject, type,  pieceName, pieceOriginalName, idRoomDestination, position);
    
  }

  public boolean addCyberLink(String id1, String id2) {
      BuildingObjectContained bo1 = this.getObjectContainedFromObj(new IdObject(id1));
      BuildingObjectContained bo2 = this.getObjectContainedFromObj(new IdObject(id2));
      
      if(bo1 == null || bo2 == null)
      {
        throw new IllegalStateException("Id not found maybe the graph is not updated");
      }
      
      BuildingObjectType t1 = bo1.getType();
      BuildingObjectType t2 = bo2.getType();
      
      if(canConnect(bo1, bo2))  
      {
        if( ! this.cyberLinkEdgeList.contains(new CyberLinkEdge(id1, id2)))
        {
          this.cyberLinkEdgeList.add( new CyberLinkEdge(id1, id2));
          return true;
        }
      }
      else
      {
        throw new IllegalArgumentException("these 2 objects can't be linked");
      }
      return false;
      
  }

  private boolean canConnect(BuildingObjectContained bo1, BuildingObjectContained bo2) {
    
    boolean ablityBased1 = bo1.getAbilities().contains(ObjectAbility.CONNECT);
    boolean ablityBased2 = bo2.getAbilities().contains(ObjectAbility.CONNECT);
    
    return ablityBased1 && ablityBased2;
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
  
  public Set<BuildingObjectContained> getSetOfBuildingObjects()
  {
    Set<BuildingObjectContained> bobs = 
        new HashSet<BuildingObjectContained>(this.objectsContained.values());
    return bobs;
    
  }
  
  
  
  /**
   * For each object contained
   * attributes and abilities are added if need
   */
  public void refreshObjectsFeautures() {

    for(BuildingObjectContained objectCont : this.objectsContained.values())
    {
        this.setAbilitiesAndAttributes(objectCont);
    }
  }

  public Set<ABACPolicy> getPolicies() {
    return policies;
  }

  public void setPolicies(Set<ABACPolicy> policies) {
    this.policies = policies;
  }
  
  public void addPolicy(ABACPolicy policy)
  {
    this.policies.add(policy);
  }
  
  public void changeIDOBJ(String oldId, String newId) {
    
    BuildingObjectContained oldObj = this.getObjectContainedFromObj(new IdObject(oldId));
    oldObj.setId(newId);
    this.removeIDObjectFromIDOBJMap(new IdObject(oldId));
    this.putObjectCont(new IdObject(newId), oldObj);
    
    //TODO: CHANGE ROOM AS WELL!!!
 
  }

  
  
  
  private List<BuildingLinkEdge> linkEdgeList = new ArrayList<BuildingLinkEdge>();
  private List<BuildingRoomNode> roomNodeList = new ArrayList<BuildingRoomNode>();
  private List<Wall> notLinkingWalls = new ArrayList<Wall>();
  private List<CyberLinkEdge> cyberLinkEdgeList = new ArrayList<CyberLinkEdge>();
  
  private Map<IdRoom, BuildingRoomNode>  buildingRooms = new HashMap<IdRoom, BuildingRoomNode>();
  private Map<IdObject, BuildingRoomNode>  objectsRoomLocation = new HashMap<IdObject, BuildingRoomNode>();
  private Map<IdObject, BuildingObjectContained> objectsContained = new HashMap<IdObject, BuildingObjectContained>();
  private Map<IdObject, BuildingObjectContained> objectsFather = new HashMap<IdObject, BuildingObjectContained>();
  
  
  private List<WrapperRect>  spaceAreasOfRooms = new ArrayList<WrapperRect>();
  /** TODO check if we can rely just on the BTREE (next line of code) **/
  private BTree<WrapperRect, String> spaceAreasTT = new BTree<WrapperRect, String>();
  
  private Set<ABACPolicy> policies = new HashSet<ABACPolicy>();
  private static BuildingSecurityGraph instance = null;
    
  
}
