package com.eteks.sweethome3d.adaptive.reachabletree;

import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.Room;

public class SecurityGraphEdge {
  private Room room;
  private List<HomePieceOfFurniture> roomObjects;
  private List<SecurityGraphEdge> neighbours;

  public SecurityGraphEdge(Room r)
  {
    this.setRoom(r);
    this.roomObjects = new ArrayList<HomePieceOfFurniture>();
    this.neighbours = new ArrayList<SecurityGraphEdge>();
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

  private String toStringTreeAcc(int level, SecurityGraphEdge tree, String acc)
  {
      if (tree.getNeighbours() == null || tree.getNeighbours().size() == 0)
        return times("\t", level) + tree.toString();
      
      String a = "";
      
      
      for (SecurityGraphEdge son : tree.getNeighbours())
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
  private boolean itsATreeAcc(SecurityGraphEdge graph, List<SecurityGraphEdge> prohibited)
  {

    List<SecurityGraphEdge> prohibitedUpd = new ArrayList<SecurityGraphEdge>();;
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

    List<SecurityGraphEdge> sons = new ArrayList<SecurityGraphEdge>(graph.getNeighbours());


    /* None of the direct sons should be one of the prohibited nodes  */
    for(SecurityGraphEdge s : sons)
    {
      if (containsRoomBasedEqual(s, prohibitedUpd) )
        return false;
    }

    /*  recursive call */
    for(SecurityGraphEdge s : sons)
    {
      if ( !  itsATreeAcc(s, prohibitedUpd))
      {
        return false;
      }

    }


    return true;
  }

  private boolean isAtree(SecurityGraphEdge graph)
  {
    return itsATreeAcc(graph, null);
  }

  public boolean containsRoomBasedEqual(SecurityGraphEdge graph, List<SecurityGraphEdge> prohibited)
  {
    if(prohibited == null || prohibited.size() == 0 )
      return false;
    if (graph == null )
      return false;

    for(SecurityGraphEdge proibNode : prohibited )
    {
      if (ReachableTreeBuillder.areEquals(graph, proibNode))
        return true;
    }

    return false;
  }


  public String rowedList(List<SecurityGraphEdge> neighbours)
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
    List<HomePieceOfFurniture> lst = new ArrayList<HomePieceOfFurniture>(this.roomObjects);
    return lst;
  }

  public void setRoomObjects(List<HomePieceOfFurniture> roomObjects) {
    this.roomObjects = roomObjects;
  }
  
  @Override
  public SecurityGraphEdge clone()
  {
    SecurityGraphEdge rg = new SecurityGraphEdge(this.room.clone());
    rg.neighbours = this.getNeighbours();
    rg.roomObjects = this.getRoomObjects();
    
    
    
    return rg;
  }
  

  public List<SecurityGraphEdge> getNeighbours() {
    List<SecurityGraphEdge>  neighboursRet = new ArrayList<SecurityGraphEdge>(this.neighbours);
    return neighboursRet;
  }

  public void addNeighbour (SecurityGraphEdge neighbour)
  {
    this.neighbours.add(neighbour);
  }

  public void addPieceOfForniture(HomePieceOfFurniture p)
  {
    this.roomObjects.add(p);
  }

  public void setNeighbours(List<SecurityGraphEdge> neighbours) {
    this.neighbours = neighbours;
  }
}
