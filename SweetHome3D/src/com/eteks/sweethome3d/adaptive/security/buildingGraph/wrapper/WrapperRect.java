package com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Rectangle3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Segment3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public class WrapperRect implements Comparable<WrapperRect> {
  
  private final Rectangle3D rect;
  private String roomID;
  
  public  WrapperRect(Rectangle3D rect) {
    this.rect = rect;
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
    
    return this.getRoomId()  + this.rect.getTopSegment().shortToString();
    
  }

  public String getRoomId() {
    return roomID;
  }

  public void setRoomId(String roomId) {
    this.roomID = roomId;
  }
  
  
  
  

}
