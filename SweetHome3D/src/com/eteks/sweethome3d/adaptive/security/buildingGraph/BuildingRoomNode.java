package com.eteks.sweethome3d.adaptive.security.buildingGraph;

import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.WrapperRect;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingGraphPart;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Shape3D;
import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.model.RoomGeoSmart;

/**
 * This class represent a room node inside a graph
 * @author Edoardo Pasi
 */
public class BuildingRoomNode extends BuildingGraphPart {
  private Room room;
  
  private List<BuildingObjectContained> objectsInside; 
  
  private Shape3D roomShape;
  
  private RoomGeoSmart roomSmart = null;
  
  /**
   * 
   * @param roomName
   * @param roomShape  the shape of the room, each coordinate should be expressed in cm
   * @param conteinedObjects
   */
  public BuildingRoomNode(String roomName, Shape3D roomShape, List<BuildingObjectContained> conteinedObjects)
  {
    
    Room roomFromShape = roomShape.getRoom();
    this.roomShape = roomShape;
    this.setName(roomName);
    
    this.room = roomFromShape;
    this.room.setName(roomName);
    
    this.setObjectsInside(conteinedObjects);
  }
  
  public void addObjectContained(BuildingObjectContained objectCont)
  {
    this.objectsInside.add(objectCont);
  }
  
  /**
   * room geo smart are more suitable for geometry purposes
   * @return
   */
  public RoomGeoSmart getRoomSmart() 
  {
    if(this.roomSmart == null)
    {  
      Room r = this.roomShape.getRoom();
      r.setName(this.room.getName());
      this.roomSmart = new RoomGeoSmart(r);
      return this.roomSmart;
    }
    else
    {
      return this.roomSmart;
    }
  
  }

  public void setId(String buildingRoomID)
  {
    this.id = buildingRoomID;
    
  }
  
  
  @Override
  public String toString()
  {
    return "Room id:" + this.id + "\n room: \n" + getRoomSmart() ;
  }

  public List<BuildingObjectContained> getObjectsInside() {
    return objectsInside;
  }

  public void setObjectsInside(List<BuildingObjectContained> objectsInside) {
    this.objectsInside = objectsInside;
  }

  public void removeObject(BuildingObjectContained objectCont) {
   this.objectsInside.remove(objectCont);
    
  }

  public WrapperRect getWrappedRect() {
    return new WrapperRect( this.getRoomSmart().getBoundingRoomRect3D());
  }
  
}
