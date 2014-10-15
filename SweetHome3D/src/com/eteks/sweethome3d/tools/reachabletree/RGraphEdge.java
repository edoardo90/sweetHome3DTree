package com.eteks.sweethome3d.tools.reachabletree;

import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.Room;

public class RGraphEdge {
  private Room room;
  private List<HomePieceOfFurniture> roomObjects;
  private List<RGraphEdge> neighbours;

  public RGraphEdge(Room r)
  {
    this.setRoom(r);
    this.roomObjects = new ArrayList<HomePieceOfFurniture>();
    this.neighbours = new ArrayList<RGraphEdge>();
  }





  @Override
  public String toString() {
    String base =  " (  "  +  this.room  +  "    ...  "  +  this.roomObjects +   " )" ;
    boolean weHaveNeigh =  (this.neighbours != null && this.neighbours.size() > 0 );
    String ext = "";      
    if (weHaveNeigh)
      ext =  "-->"   + rowedList(this.neighbours) ;

    return base + ext + "\n";

  }

  public String toStringShallow()
  {
    return "(" + this.room + ")";
  }

  public String toStringTree()
  {
    if ( ! isAtree(this))
      return this.toString();

    return this.toStringTreeAcc(0, this, "");

  }

  private String toStringTreeAcc(int level, RGraphEdge tree, String acc)
  {
      if (tree.getNeighbours() == null || tree.getNeighbours().size() == 0)
        return times("\t", level) + tree.toString();
      
      String a = "";
      
      
      for (RGraphEdge son : tree.getNeighbours())
      {
        a =  a + times("\t", level) + "\n" +
             times("\t", level)  + tree.toStringShallow() + 
              "\n" + 
             toStringTreeAcc(level + 1, son, acc);
      }
      
      
      return acc + a;
  }
  
  private  String times(String s, int times)
  {
    String a = "";
    for(int i=0; i<times; i++)
      a = a + s;
    return a;
  }
  

  /**
   * A graph is a tree if visiting like it were a tree, you can never find a son equal to an ancestor
   *        <pre>    
   *                          1
   *                     2        3
   *                 5     6       4   2
   *                 is a tree
   *                 while  
   *                           1
   *                    2           3
   *                 1    4      5       6     </pre>
   *                 is <b> not </b> even if it looks like one  (the 1 son of the 2 have 1 as ancestor)
   * @param graph: the graph we want to test
   * @param prohibited:  used as accumulator, for recursive call
   * @return  true if the graph is a tree, false otherwise
   */
  private boolean itsATreeAcc(RGraphEdge graph, List<RGraphEdge> prohibited)
  {

    List<RGraphEdge> prohibitedUpd = new ArrayList<RGraphEdge>();;
    /* NULL leaf  */
    if (graph == null)
      return true;


    /* HEAD is prohibited  (i.e. is in the list of ancestors?) */
    if ( containsRoomBasedEqual( graph, prohibited)  )
      return false;
    if (graph.getNeighbours() == null || graph.getNeighbours().size() == 0)
      return true;

    /* Propagate the ancestors, and call recursively */
    if(prohibited != null) 
      prohibitedUpd.addAll(prohibited);
    prohibitedUpd.add(graph);

    List<RGraphEdge> sons = new ArrayList<RGraphEdge>(graph.getNeighbours());


    /* None of the direct sons should be one of the prohibited nodes  */
    for(RGraphEdge s : sons)
    {
      if (containsRoomBasedEqual(s, prohibitedUpd) )
        return false;
    }

    /*  recursive call */
    for(RGraphEdge s : sons)
    {
      if ( !  itsATreeAcc(s, prohibitedUpd))
      {
        return false;
      }

    }


    return true;
  }

  private boolean isAtree(RGraphEdge graph)
  {
    return itsATreeAcc(graph, null);
  }

  public boolean containsRoomBasedEqual(RGraphEdge graph, List<RGraphEdge> prohibited)
  {
    if(prohibited == null || prohibited.size() == 0 )
      return false;
    if (graph == null )
      return false;

    for(RGraphEdge proibNode : prohibited )
    {
      if (ReachableTreeBuillder.areEquals(graph, proibNode))
        return true;
    }

    return false;
  }


  public String rowedList(List<RGraphEdge> neighbours)
  {

    String accumString = ""; 
    int i;
    if( neighbours == null || neighbours.size() == 0 )
      return "";
    for( i = 0; i<neighbours.size() - 1; i++)
    {
      accumString = accumString +  neighbours.get(i).toStringShallow() + "-->";
    }
    accumString = accumString + neighbours.get(i).toStringShallow();
    return accumString;
  }

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public List<HomePieceOfFurniture> getRoomObjects() {
    return roomObjects;
  }

  public void setRoomObjects(List<HomePieceOfFurniture> roomObjects) {
    this.roomObjects = roomObjects;
  }

  public List<RGraphEdge> getNeighbours() {
    List<RGraphEdge>  neighboursRet = new ArrayList<RGraphEdge>(this.neighbours);
    return neighboursRet;
  }

  public void addNeighbour (RGraphEdge neighbour)
  {
    this.neighbours.add(neighbour);
  }

  public void addPieceOfForniture(HomePieceOfFurniture p)
  {
    this.roomObjects.add(p);
  }

  public void setNeighbours(List<RGraphEdge> neighbours) {
    this.neighbours = neighbours;
  }
}
