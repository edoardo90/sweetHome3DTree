package com.eteks.sweethome3d.tools.security.buildingGraph;

import java.util.List;

import com.eteks.sweethome3d.model.RectangularRoom;
import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.tools.security.buildingGraphObjects.BuildingGraphPart;
import com.eteks.sweethome3d.tools.security.ifcSecurity.IfcSecurityExtractor.Rectangle3D;

public class BuildingRoomNode extends BuildingGraphPart {
  private Room room;
  
  private List<Object> objectsInside;  //TODO: think something more specific :P
  
  private Rectangle3D roomShape;
  
  public BuildingRoomNode(Rectangle3D roomShape, List<Object> conteinedObjects) {
    RectangularRoom rr = new RectangularRoom(roomShape);
    this.setRoom(rr.getRoom());
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

  public List<Object> getObjectsInside() {
    return objectsInside;
  }

  public void setObjectsInside(List<Object> objectsInside) {
    this.objectsInside = objectsInside;
  }
  
}
