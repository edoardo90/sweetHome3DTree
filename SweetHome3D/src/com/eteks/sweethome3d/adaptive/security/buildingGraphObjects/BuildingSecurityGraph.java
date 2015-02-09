package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingLinkEdge;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingRoomNode;
import com.eteks.sweethome3d.model.Wall;

/**
 * Data Structure used to represent the graph associated to the building
 * @author 
 */
public class BuildingSecurityGraph {
    
  private List<BuildingLinkEdge> linkEdgeList;
  private List<BuildingRoomNode> roomNodeList;
  private List<Wall> notLinkingWalls ;
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
  public List<Wall> getNotLinkingWalls() {
    return notLinkingWalls;
  }
  public void setNotLinkingWalls(List<Wall> notLinkingWalls) {
    this.notLinkingWalls = notLinkingWalls;
  }
  
  

}
