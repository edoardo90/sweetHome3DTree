package com.eteks.sweethome3d.adaptive.treetobuilding;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.graphstream.graph.Node;

import com.eteks.sweethome3d.model.Room;


class GraphicInfoNode implements Comparable<GraphicInfoNode>
{
  private SortedSet<GraphicInfoNode> neighAbove = new TreeSet<GraphicInfoNode>();
  private SortedSet<GraphicInfoNode> neighDown = new TreeSet<GraphicInfoNode>();
  private SortedSet<GraphicInfoNode> neighLeft = new TreeSet<GraphicInfoNode>();
  private SortedSet<GraphicInfoNode> neighRigh = new TreeSet<GraphicInfoNode>();

  private Node baseNode ;
  private String baseNodeId;
  private float x, y;

  @SuppressWarnings("unused")
  private Map<String, double []> nodesPositions;
  private boolean isDummy = false;

  private static final int IGRAPH_TO_ROOM_SIZE = 400;

  private int max(int x, int y) { return x > y ? x : y; }

  public GraphicInfoNode(Node n,  Map<String, double []> nodesPositions)
  {
    
    if(nodesPositions == null)
    {
      System.out.println(" ");
    }
    
    this.nodesPositions = nodesPositions;

    GraphicInfoNode gin = transformNodeIntoInfo(n, nodesPositions);

    this.baseNode = gin.baseNode;
    this.baseNodeId = gin.baseNodeId;

    this.neighAbove = gin.neighAbove;
    this.neighDown = gin.neighDown;
    this.neighLeft = gin.neighLeft;
    this.neighRigh = gin.neighRigh;

    this.x = gin.x;
    this.y = gin.y;

  }

  private GraphicInfoNode()
  {
      this.neighAbove = new TreeSet<GraphicInfoNode>();
      this.neighDown =  new TreeSet<GraphicInfoNode>();
      this.neighLeft =  new TreeSet<GraphicInfoNode>();
      this.neighRigh =  new TreeSet<GraphicInfoNode>();
  }

  private GraphicInfoNode transformNodeIntoInfo(Node n, Map<String, double []> nodesPositions2) 
  {

    GraphicInfoNode gin = new GraphicInfoNode();
    gin.baseNode = n;
    gin.baseNodeId = gin.baseNode.getId();
    
    
    Iterator<Node> neighIter = gin.baseNode.getNeighborNodeIterator();
    
    
    double x = nodesPositions2.get(gin.baseNodeId)[0];
    double y = nodesPositions2.get(gin.baseNodeId)[1];
    gin.x  = (float) ( x);
    gin.y = (float) ( y );


    while(neighIter.hasNext())
    {
      Node neigh = neighIter.next();
      Double xn = nodesPositions2.get(neigh.getId())[0];
      Double yn = nodesPositions2.get(neigh.getId())[1];

      double xvect = xn - x;
      double yvect = y - yn;

      double tetaRad = Math.atan2(yvect, xvect);  // teta in  -pi +pi
      double teta = Math.toDegrees(tetaRad);
      if(teta < 0 ) 
          teta = 360 + teta;
      if(teta >= 45 && teta < (45 + 90))
        gin.addAbove(neigh, nodesPositions2);

      if(teta >= (90 + 45) && teta < (180 + 45))
        gin.addLeft(neigh, nodesPositions2);

      if(teta >= (180 + 45) && teta < (270 + 45))
        gin.addDown(neigh, nodesPositions2);

      if(teta >= (270 + 45) || (teta < 45 && teta >= 0))
        gin.addRight(neigh, nodesPositions2);

    }
    return gin;
  }

  public int getWidth()
  {
    return this.getRectangle().width;
  }
  public int getHeight()
  {
    return this.getRectangle().height;
  }

  public String getId()
  {
    return this.baseNode.getId();
  }

  @Override
  public String toString()
  {
    if (this.isDummy)
      return this.getId();
    return this.getId() + "\n  above : " + this.neighAbove + " \n" +
                            "  left  : " + this.neighLeft + "\n"+
                            "  righ  : " + this.neighRigh + "\n" +
                            "  down  : " + this.neighDown ;
     
  }

  private void addLeft(Node n, Map<String, double []> nodesPositions2)
  {
    GraphicInfoNode gin  = this.getDummyNode(n, nodesPositions2);
    this.neighLeft.add(gin);
  }
  private void addRight(Node n, Map<String, double []> nodesPositions2)
  {
    GraphicInfoNode gin  = this.getDummyNode(n, nodesPositions2);
    this.neighRigh.add(gin);
  }
  private void addDown(Node n, Map<String, double []> nodesPositions2)
  {
    GraphicInfoNode gin  = this.getDummyNode(n, nodesPositions2);
    this.neighDown.add(gin);
  }

  private void addAbove(Node n, Map<String, double []> nodesPositions2)
  {

    GraphicInfoNode gin   = this.getDummyNode(n, nodesPositions2);
    this.neighAbove.add(gin);
  }

  private GraphicInfoNode getDummyNode(Node n, Map<String, double []> nodesPositions2) {
    GraphicInfoNode ginDummy = new GraphicInfoNode();
    ginDummy.x = (float) nodesPositions2.get(n.getId())[0];
    ginDummy.y = (float) nodesPositions2.get(n.getId())[1];
    ginDummy.baseNode = n;
    ginDummy.baseNodeId = n.getId();
    ginDummy.nodesPositions = null;
    ginDummy.isDummy = true;
    
    return ginDummy;
}
  

  private Rectangle getRectangle()
  {

    int height =  max (neighLeft.size(), neighRigh.size());
    int width =  max (neighAbove.size(), neighDown.size());

    return new Rectangle(max(1, width),  max(1,height));

  }
  

  
  public Rectangle getRoomRectangle()
  {
    Rectangle r = this.getRectangle();

    float x1 = (float) this.x * IGRAPH_TO_ROOM_SIZE * 6;
    float y1 =   this.y * IGRAPH_TO_ROOM_SIZE  * 6;
        
    float x2 = x1 + (float) ( r.width) * IGRAPH_TO_ROOM_SIZE;
    float y2 = y1 + (float) ( r.height) * IGRAPH_TO_ROOM_SIZE;
    
    return new Rectangle( (int) (x1), (int)(y1), (int)Math.abs(x2 - x1) , (int)Math.abs(y2 - y1) );
  }
  public Room getRoom()
  {
    Rectangle r = this.getRectangle();
    
    float x1 =   this.x * IGRAPH_TO_ROOM_SIZE *  6 ;
    float y1 =   this.y * IGRAPH_TO_ROOM_SIZE  * 6;

   
    
    float x2 = x1;
    float x4 = x1 + (float) (( r.width) * IGRAPH_TO_ROOM_SIZE);
    float x3 = x4;
    
    float y4 = y1;
    float y2 = y1 + (float) (( r.height) * IGRAPH_TO_ROOM_SIZE);
    float y3 = y2;
    

    float [][] points = new float [4][2];

    points[0][0] = x1;
    points[0][1] = y1;
    points[1][0] = x2;
    points[1][1] = y2;

    points[2][0] = x3;
    points[2][1] = y3;
    points[3][0] = x4;
    points[3][1] = y4;
    
    

    Room room = new Room(points);

    return room;
  }


  public float getYnode() {
    return y;
  }

  public float getXnode() {
    return x;
  }







  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.baseNodeId == null)
        ? 0
        : this.baseNodeId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GraphicInfoNode other = (GraphicInfoNode)obj;
    if (this.baseNodeId == null) {
      if (other.baseNodeId != null)
        return false;
    } else if (!this.baseNodeId.equals(other.baseNodeId))
      return false;
    return true;
  }

  public int getCountOfNeigh(Direction dir)
  {
      return  this.getNeighsInDirection(dir).size();
  }
  
  public int getPositionOfNeigh(GraphicInfoNode neigh, final Direction dir)
  {
     List<GraphicInfoNode> neighs = new ArrayList<GraphicInfoNode>( this.getNeighsInDirection(dir));
     Collections.sort(neighs, new Comparator<GraphicInfoNode>()
     {

      public int compare(GraphicInfoNode o1, GraphicInfoNode o2) {
         if(dir == Direction.up || dir == Direction.down)
         {
           return ((Float)o1.x).compareTo(o2.x);
         }
         else
         {
           return ((Float)o1.y).compareTo(o2.y);
         }
      }
       
     });
     
     return neighs.indexOf(neigh);
  }

  private SortedSet<GraphicInfoNode> getNeighsInDirection(Direction dir)
  {
    switch(dir)
    {
    case down:
      return this.neighDown;
    case up:
      return this.neighAbove;

    case left:
      return this.neighLeft;

    case right:
      return this.neighRigh;
    default:
        return null;
    }
  }

  public Direction getDirectionOfNeighbour(GraphicInfoNode graphInfoNeigh) {

    if (this.neighAbove.contains(graphInfoNeigh))
      return Direction.up;

    if (this.neighDown.contains(graphInfoNeigh))
      return Direction.down ;

    if (this.neighLeft.contains(graphInfoNeigh))
      return Direction.left;

    if (this.neighRigh.contains(graphInfoNeigh))
      return Direction.right;

    return null;
  }

  public int compareTo(GraphicInfoNode other) {
    Float yy = this.y;
    Float xx = this.x;
    
    if(yy == other.y)
    {
      return xx.compareTo(other.x);
    }
    else
    {
      return yy.compareTo(other.y);
    }
    
  }


}