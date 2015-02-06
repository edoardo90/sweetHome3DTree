package com.eteks.sweethome3d.adaptive.security.buildingGraph;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.DoorObject;
import com.eteks.sweethome3d.model.Wall;

public class BuildinLinkWallWithDoor extends BuildingLinkWall {

  private DoorObject door;
  public BuildinLinkWallWithDoor(Wall wall, DoorObject door, String room1, String room2) {
    super(wall, room1, room2);
    this.door = door;
   
  }

}
