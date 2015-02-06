package com.eteks.sweethome3d.adaptive.security.buildingGraph;

import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingGraphPart;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Shape3D;
import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.model.RoomGeoSmart;


public class BuildingRoomNode extends BuildingGraphPart {
  private Room room;
  
  private List<BuildingObjectContained> objectsInside; 
  
  private Shape3D roomShape;
  
  public BuildingRoomNode(String roomName, Shape3D roomShape, List<BuildingObjectContained> conteinedObjects)
  {
    
    Room roomFromShape = roomShape.getRoom();
    this.roomShape = roomShape;
    this.name = roomName;
    
    this.room = roomFromShape;
    this.room.setName(roomName);
    
    this.setObjectsInside(conteinedObjects);
  }

  public RoomGeoSmart getRoom() 
  {
    Room r = this.getRoom();
    return new RoomGeoSmart(r);
  
  }

  public void setId(String buildingRoomID)
  {
    this.id = buildingRoomID;
    
  }
  
  
  @Override
  public String toString()
  {
    return "name:" + this.name + "id:" + this.id + "\n shape:" + roomShape + "\n";
  }

  public List<BuildingObjectContained> getObjectsInside() {
    return objectsInside;
  }

  public void setObjectsInside(List<BuildingObjectContained> objectsInside) {
    this.objectsInside = objectsInside;
  }
  
}
