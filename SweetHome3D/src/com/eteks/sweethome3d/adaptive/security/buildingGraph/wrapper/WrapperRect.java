package com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.eteks.sweethome3d.adaptive.security.assets.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingRoomNode;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Rectangle3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Segment3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public class WrapperRect implements Comparable<WrapperRect>, Serializable {
  private final Rectangle3D rect;
  private String roomID;
  private String roomName;

  public  WrapperRect(Rectangle3D rect) {
    this.rect = rect;
  }

  public WrapperRect(float x, float y) {
    this.rect = new Rectangle3D(new Vector3D(x, y, 0), 10, 10);
  }

  public WrapperRect(Vector3D position) {
    this((float)position.first, (float)position.second);
  }

  @Override
  public boolean equals(Object o)
  {
    WrapperRect other;
    if(o instanceof WrapperRect)
    {
      other = (WrapperRect) o;

      Segment3D otherTop = other.rect.getTopSegment();
      Segment3D myTop = this.rect.getTopSegment();
      return (myTop.contains(otherTop) || otherTop.contains(myTop));

    }
    else
    {
      if (o instanceof Vector3D) {
        Vector3D position = (Vector3D)o;
        return this.rect.contains(position);
      }
    }

    return false;
  }

  /**
   * ^   1   2   3
   * |   4   5   6
   * |
   * y
   * |
   * -----x-------->
   */
  public int compareTo(WrapperRect o) {

    Segment3D topSeg1 = rect.getTopSegment();
    Segment3D topSeg2 = o.rect.getTopSegment();

    Rectangle3D myRect = this.rect;
    Rectangle3D otherRect = o.rect;

    if(this.rect.contains(otherRect) || otherRect.contains(this.rect))
    {
      return 0;
    }

    if(topSeg1.isUpperThen(topSeg2))
      return 1;
    if(topSeg2.isUpperThen(topSeg1))
      return -1;

    if(topSeg2.isRighterThen(topSeg1))
      return 1;
    if(topSeg1.isRighterThen(topSeg2))
      return -1;

    return 0;

  }



  @Override
  public String toString()
  {
    String contained = "";
    BuildingSecurityGraph segraph = BuildingSecurityGraph.getInstance();
    BuildingRoomNode broom = segraph.getBuildingRoomFromRoom(new IdRoom(this.getRoomId()));
    
    if(broom != null)
    {
      contained +=  "  Contains:  [" ;
      for(BuildingObjectContained boc : broom.getObjectsInside())
      {
        contained += boc.getId() + ", ";
      }
      contained += " ]";
    }
    
    return this.roomName + contained;  

  }

  public String getRoomId() {
    return roomID;
  }

  public void setRoomId(String roomId) {
    this.roomID = roomId;
  }

  public Vector3D getFreeCoordinate() {
    List<Vector3D> points = this.rect.getListOfPoints();
    
    double BORDER = 40;
    
    double xmax = points.get(0).first - BORDER;
    double xmin = points.get(1).first + BORDER;

    double ymax = points.get(0).second - BORDER ; 
    double ymin = points.get(2).second + BORDER;

    Vector3D proposedCoord = null;
    String roomIdAtProposedPosition = null;
    Random rand = new Random();
    double x = (Double) ( rand.nextInt((int)((xmax - xmin) + 1)) + xmin );
    double y = (Double) ( rand.nextInt((int)((ymax - ymin) + 1)) + ymin );

    proposedCoord = new Vector3D(x, y, 0);


    return proposedCoord;

  }

  public String getName()
  {
    return this.roomName;
  }

  public void setRoomName(String name) {
    this.roomName = name;

  }


}
