package com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Rectangle3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Segment3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public class WrapperRect implements Comparable<WrapperRect>, Serializable, ExpressiveStrings {
  private final Rectangle3D rect;
  private String roomID;
  private String roomName;
  private Map<String, Vector3D> positions = new HashMap<String, Vector3D>();
  private Map<Vector3D, String> positionsRev = new HashMap<Vector3D, String>();
  
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
    
      return this.roomName + "positions:" + this.positions;
    
  }

  public String getRoomId() {
    return roomID;
  }

  public void setRoomId(String roomId) {
    this.roomID = roomId;
  }

  public void add(BuildingObjectContained objectCont, Vector3D position) {
    this.positions.put(objectCont.getId(), position);
    this.positionsRev.put(position, objectCont.getId());
    
  }

  public Vector3D getFreeCoordinate() {
      List<Vector3D> points = this.rect.getListOfPoints();
      
      double xmax = points.get(0).first;
      double xmin = points.get(1).first;
      
      double ymax = points.get(0).second;
      double ymin = points.get(2).second;
     
      Vector3D proposedCoord = null;
      String roomIdAtProposedPosition = null;
      do
      {
        Random rand = new Random();
        double x = (Double) ( rand.nextInt((int)((xmax - xmin) + 1)) + xmin );
        double y = (Double) ( rand.nextInt((int)((ymax - ymin) + 1)) + ymin );
        
        proposedCoord = new Vector3D(x, y, 0);
        roomIdAtProposedPosition = this.positionsRev.get(proposedCoord);
      }
      while(roomIdAtProposedPosition != null);
      return proposedCoord;
      
  }

  public String getContainStirng() {
   String s = "";
   s = s + "[" + this.getName() + "]  NW: " + this.rect.getPointNorthWest() + 
       " " + this.positions;  
   return s;
  }

  public String getName()
  {
    return this.roomName;
  }
  
  public void setRoomName(String name) {
     this.roomName = name;
    
  }
  
  
  
  

}
