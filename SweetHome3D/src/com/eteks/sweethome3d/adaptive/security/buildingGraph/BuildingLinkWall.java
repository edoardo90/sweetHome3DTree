package com.eteks.sweethome3d.adaptive.security.buildingGraph;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.model.Wall;

public class BuildingLinkWall extends BuildingLinkEdge {

   private Wall wall;
  
   public BuildingLinkWall(Vector3D position, String room1, String room2) {
       super(position, room1, room2);
   }

  public Wall getWall() {
    return wall;
  }

  public void setWall(Wall wall) {
    this.wall = wall;
  }

}
