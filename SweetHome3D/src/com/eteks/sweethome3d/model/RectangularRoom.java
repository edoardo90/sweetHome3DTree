package com.eteks.sweethome3d.model;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Rectangle3D;



public class RectangularRoom  {

  
  private Room room;
  
  public RectangularRoom(Rectangle3D rect)
  {
    float x0, y0, x1,y1, x2,y2, x3,y3;

    x0=(float)rect.getPointNorthWest().first;
    y0=(float)rect.getPointNorthWest().second;

    x2= (float)rect.getPointSouthEast().first;
    y2= (float)rect.getPointSouthEast().second;

    x1 = x2;
    y1 = y0;

    x3 = x0;
    y3 = y2;

    //non clockwise
    this.room = new Room(new float[][] {{x0, y0}, {x1, y1}, {x2, y2}, {x3, y3} });
  }

  public Room getRoom() {
    return room;
  }


}
