package com.eteks.sweethome3d.tools.treetobuilding;

import java.awt.Rectangle;

import com.eteks.sweethome3d.model.HomeDoorOrWindow;
import com.eteks.sweethome3d.model.Room;

public class Corridor {
  private Room corridorShape;
  private HomeDoorOrWindow firstDoor;
  private HomeDoorOrWindow secondDoor;
  private String graphInfoNeighID;
  private String graphicNodeID;

  private float xNeigh;
  private float yNeigh;

  private float xInfo;
  private float yInfo;

  public Corridor(GraphicInfoNode graphInfoNeigh, GraphicInfoNode graphicNode, Direction directionOfNeighbour, HomeDoorOrWindow door)
  {
    this.graphicNodeID = graphicNode.getId();
    this.graphInfoNeighID = graphInfoNeigh.getId();

    if(door != null)
    {
      this.firstDoor = door.clone();
      this.secondDoor = firstDoor.clone();
    }

    this.xInfo = graphicNode.getXnode();
    this.yInfo = graphicNode.getYnode();

    this.xNeigh = graphInfoNeigh.getXnode();
    this.yNeigh = graphInfoNeigh.getYnode();

    constr(graphInfoNeigh, graphicNode, directionOfNeighbour);
  }

  public Corridor(String idNode, String idNodeNeigh)
  {
    this.graphicNodeID = idNode;
    this.graphInfoNeighID = idNodeNeigh;
  }

  private void constr(GraphicInfoNode graphInfoNeigh, GraphicInfoNode graphicNode, Direction directionOfNeighbour)
  {
    if (directionOfNeighbour == Direction.down )
      constr(graphicNode, graphInfoNeigh, Direction.up);

    if (directionOfNeighbour == Direction.right)
      constr(graphicNode, graphInfoNeigh, Direction.left);

    //now i can handle up and left cases

    //up:   my neighbour is above me

    /*                                         
     *                                    pos = 2
     *      posN = 0         posN = 1       
     *                                 |neigh3|
     *       |  neigh1 |    |neigh2|    /
     *         /  \          |         /
     *       /     \         |        /
     *  |other|    |         me       |
     *
     *   posM = 0         posM=1                  
     */

    //in which position is the neigh respect of me    e.g.   me, neigh3  =  2
    int posOfNeigh = graphicNode.getPositionOfNeigh(graphInfoNeigh, directionOfNeighbour);

    //in which position am I for the neigh            e.g.   me,  neigh1 = 1
    int posOfGrapNode = graphInfoNeigh.getPositionOfNeigh(graphicNode, Direction.opposite(directionOfNeighbour));

    Rectangle rectGraph =  graphicNode.getRoomRectangle();
    Rectangle rectGinfoNeigh = graphInfoNeigh.getRoomRectangle();


    int neighCountTowards = graphicNode.getCountOfNeigh(directionOfNeighbour);

    int neighCountNeigh   = graphInfoNeigh.getCountOfNeigh(Direction.opposite(directionOfNeighbour));

    if(directionOfNeighbour == Direction.up)
    {
      //divide mine upper edge in parts


      //                 0   posOfGrahpNode 1

      //    neigh      |------\--|---------|     ---------- is  segmentSizeNeigh
      //                       \     
      //                         \
      //    me       | ---- |  --\-- | ---- |    ----  is   segmentSizeMe

      //                          1
      //                 0    posOfNeigh   2

      float upperEdgeSize =  rectGraph.width;
      float segmentSizeMe =    upperEdgeSize / neighCountTowards;


      float lowerNeighEdgeSize = rectGinfoNeigh.width;
      float segmentSizeNeigh =   lowerNeighEdgeSize / neighCountNeigh;

      /* 
       *   x2,y2       x3,y3
       *      _____________
       *      \            \
       *       \            \
       *        \            \
       *         \            \ 
       *          --------------     
       *          x0,y0       x1,y1
       * 
       */

      float x0 = (float) rectGraph.getX() + segmentSizeMe * posOfNeigh;
      float y0 = (float) rectGraph.getY();
      float x1 = x0 + segmentSizeMe * (posOfNeigh + 1);
      float y1 = y0;

      float x2 = (float) rectGinfoNeigh.getX() + segmentSizeNeigh * posOfGrapNode;
      float y2 = (float) rectGinfoNeigh.getY() + rectGinfoNeigh.height;
      float x3 = x2 + segmentSizeNeigh * (posOfGrapNode + 1);
      float y3 = y2;

      //non clockwise
      Room corridorShape = new Room(new float[][] {{x0, y0}, {x1, y1}, {x3, y3}, {x2, y2} });
      this.corridorShape = corridorShape;

      if(this.firstDoor != null)
      {
        //door y=y2  x in x2, x3
        this.firstDoor.setX( (x2+x3)/2);
        this.firstDoor.setY(y2);
        this.firstDoor.setAngle(0);
        //door  y=y1  x in x0, x1
        this.secondDoor.setY(y1);
        this.secondDoor.setX((x0+x1)/2);
        // door.angle = 0
        this.secondDoor.setAngle(0);
      }

    }
    else if (directionOfNeighbour == Direction.left)
    {
      //divide the left edge in parts
      float lefterEdgeSize =  rectGraph.height;
      float segmentSizeMe =    lefterEdgeSize / neighCountTowards;


      float righterNeighEdgeSize = rectGinfoNeigh.height;
      float segmentSizeNeigh =   righterNeighEdgeSize / neighCountNeigh;

      float x0 = (float) rectGraph.getX();
      float y0 = (float) rectGraph.getY()  + segmentSizeMe * posOfNeigh ;
      float x1 = x0 ;
      float y1 = y0 + segmentSizeMe ;

      float x2 = (float) rectGinfoNeigh.getX() + rectGinfoNeigh.width;
      float y2 = (float) rectGinfoNeigh.getY()  + segmentSizeNeigh * posOfGrapNode;;
      float x3 = x2 ;
      float y3 = y2 + segmentSizeNeigh ;

      /*
       *      x2 y2
       *        |  \
       *        |   \
       *        |    \ 
       *  x3 y3 \     \
       *         \     \
       *          \     \  x0 y0
       *           \    |
       *            \   |
       *             \  | x1 y1
       * 
       */



      Room corridorShape = new Room(new float[][] {{x0, y0}, {x1, y1}, {x3, y3}, {x2, y2} });


      if(this.firstDoor!= null)
      {
        this.firstDoor.setX(x2);
        this.firstDoor.setY((y2+y3)/2);
        this.firstDoor.setAngle((float) Math.PI/2);


        this.secondDoor.setX(x1);
        this.secondDoor.setY( (y1 + y0) / 2);
        this.secondDoor.setAngle((float) Math.PI/2);
      }
      // door.angle = 270

      this.corridorShape = corridorShape;

    }

  }

  private float max(float a, float b)
  {
    return a > b ? a : b;
  }
  
  
  @Override
  public String toString()
  {

    float xMax = max(xNeigh, xInfo);
    float yMax = max(xNeigh, yInfo);

    if(xMax != 0)
    {
      xInfo = xInfo / xMax ;
      xNeigh = xNeigh / xMax;
    }
    if (yMax != 0)
    {
      yInfo = yInfo / yMax;
      yNeigh = yNeigh / yMax;
    }

    yInfo = yInfo   * 10;
    yNeigh = yNeigh * 10;
    xNeigh = xNeigh * 10;
    xInfo = xInfo   * 10;

    String s = "";
    for(int i=0; i<12; i++)
    {
      for(int j=0; j<12; j++)
      {
        if(i==xNeigh && j== yNeigh)
        {
          s = s + this.graphInfoNeighID;
        }
        else if (i==xInfo && j== yInfo)
        {
          s = s + this.graphicNodeID;
        }
        else
        {
          s = s + " ";
        }
      }
      s = s + "\n";
    }

    return s;

  }




  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.graphInfoNeighID == null)
        ? 0
            : this.graphInfoNeighID.hashCode());
    result = prime * result + ((this.graphicNodeID == null)
        ? 0
            : this.graphicNodeID.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Corridor)) {
      return false;
    }
    Corridor other = (Corridor)obj;
    if (this.graphInfoNeighID == null) {
      if (other.graphInfoNeighID != null) {
        return false;
      }
    } else if (!this.graphInfoNeighID.equals(other.graphInfoNeighID)) {
      return false;
    }
    if (this.graphicNodeID == null) {
      if (other.graphicNodeID != null) {
        return false;
      }
    } else if (!this.graphicNodeID.equals(other.graphicNodeID)) {
      return false;
    }
    return true;
  }

  public HomeDoorOrWindow getSecondDoor() {
    return secondDoor;
  }

  public HomeDoorOrWindow getFirstDoor() {
    return firstDoor;
  }

  public Room getCorridorShape() {
    return corridorShape;
  }


}
