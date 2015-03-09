package com.eteks.sweethome3d.adaptive.security.buildingGraph;

import java.io.Serializable;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.DoorObject;
import com.eteks.sweethome3d.model.Wall;

/**
 * A link inside the graph, a wall with a door
 * @author Edoardo Pasi
 */

public class BuildinLinkWallWithDoor extends BuildingLinkWall implements Serializable {

  private DoorObject door;
  
  /**
   * This constructor assign the id basing on the {@link DoorObject} object
   * @param wall
   * @param door
   * @param room1
   * @param room2
   */
  public BuildinLinkWallWithDoor(Wall wall, DoorObject door, String room1, String room2) {
    super(wall, room1, room2);
    this.setDoor(door);
    this.id = door.getId();
   
  }
  
  @Override
  public String toString()
  {
    if(this.firstRoom.compareTo(this.secondRoom)<0)
    {
      return this.firstRoom + "<--- " + "Doord ID: " + this.getId() + " --->" +  this.secondRoom + "\n";
    }
    else
    {
      return this.secondRoom + "<--- " + "Doord ID: " + this.getId() + " --->" +  this.firstRoom + "\n";
    }
  }

  public DoorObject getDoor() {
    return door;
  }

  public void setDoor(DoorObject door) {
    this.door = door;
  }
  
  

  
  

}
