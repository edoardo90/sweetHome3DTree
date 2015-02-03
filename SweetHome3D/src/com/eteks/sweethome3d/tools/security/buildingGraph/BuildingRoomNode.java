package com.eteks.sweethome3d.tools.security.buildingGraph;

import java.util.List;

import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.tools.security.buildingGraphObjects.BuildingGraphPart;
import com.eteks.sweethome3d.tools.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.tools.security.parserobjects.Shape3D;


public class BuildingRoomNode extends BuildingGraphPart {
  private Room room;
  
  private List<BuildingObjectContained> objectsInside; 
  
  private Shape3D roomShape;
  
  public BuildingRoomNode(Shape3D roomShape, List<BuildingObjectContained> conteinedObjects)
  {
    
    Room roomFromShape = roomShape.getRoom();
    
    this.setRoom(roomFromShape);
    
    this.setObjectsInside(conteinedObjects);
  }

  public Room getRoom() {
    return room.clone();
  }

  public void setRoom(Room room) {
    this.room = room;
  }
  
  @Override
  public String toString()
  {
    return "" + roomShape;
  }

  public List<BuildingObjectContained> getObjectsInside() {
    return objectsInside;
  }

  public void setObjectsInside(List<BuildingObjectContained> objectsInside) {
    this.objectsInside = objectsInside;
  }
  
}
