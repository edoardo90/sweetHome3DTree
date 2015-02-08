package com.eteks.sweethome3d.adaptive.security.buildingGraph;

import com.eteks.sweethome3d.model.Wall;

public class BuildingLinkWall extends BuildingLinkEdge {

   private final Wall wall;
   
   public BuildingLinkWall(Wall wall,  String room1, String room2) {
       super(room1, room2);
       this.wall = wall;
   }

  public Wall getWall() {
    return wall;
  }

  @Override
  public String toString()
  {
    return firstRoom + "<--" + "Wall ID:" + this.getId() +  "---->" +  secondRoom + "\n";
  }


}
