package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingLinkEdge;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingRoomNode;

public class BuildingSecurityGraph {
    
  private List<BuildingLinkEdge> linkEdgeList;
  private List<BuildingRoomNode> roomNodeList;
  public List<BuildingLinkEdge> getLinkEdgeList() {
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
  }
  
  

}
