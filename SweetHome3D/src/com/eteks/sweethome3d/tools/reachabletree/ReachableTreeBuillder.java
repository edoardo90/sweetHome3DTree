package com.eteks.sweethome3d.tools.reachabletree;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.graphstream.graph.Graph;

import com.eteks.sweethome3d.model.HomeDoorOrWindow;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.model.Wall;
import com.eteks.sweethome3d.tools.Conversions;

/**
 * This class is used to build the Tree of Reachability
 * <br/> <br/>
 * This tree starts from a room, for example the entrance, and represent every (non circular)
 * path from the entrance to all other rooms.  Also non minimal path are represented. <br/> <br/>
 * 
 * As first thing we build the Reachability graph  G = &lt E, V &gt, where V is the set of vertices
 * and each vertex is a room, including all the fornitures inside it and E is the set of edges,
 * there is an edge between tho vertices iif they are linked by a door (or by a stair in the future)
 * 
 * @author Edoardo
 */

public class ReachableTreeBuillder {
  
  List<Wall> walls; 
  List<Room> rooms;
  List<HomePieceOfFurniture> fornitures;

  public ReachableTreeBuillder(List<Wall> walls, List<Room> rooms, List<HomePieceOfFurniture> fornitures) 
  {
    this.walls = walls;
    this.rooms = rooms;
    this.fornitures = fornitures ;
  }
  
  
  public String getAsciiArtTree()
  {
    
    RGraphEdge threachTree = makeReachTree();
    if(threachTree == null )
      return "";
    String s = threachTree.toString();
    s = threachTree.toStringTree();
    s = s + "";
    
    return s;
  }
  
  private RGraphEdge makeReachTree()
  {
    Set<RGraphEdge> rgraph = this.makeGraph();
    List<RGraphEdge> rgraphOrd = new ArrayList<RGraphEdge>(rgraph);
    
    RGraphEdge ev = null;
    
    if(rgraphOrd != null && rgraphOrd.size() > 0)
         ev = rgraphOrd.get(0);
    else
         return null;
    for(RGraphEdge e : rgraphOrd)
    {
      if (e.getRoom().getName().equals("room 1"))
        ev = e;
    }
    
    
    RGraphEdge thereachTree = BT(ev, null);
    
    return thereachTree;
  }
  
  protected Set<RGraphEdge> makeGraph()
  {
    /*  1) Graph <V, E>  where V are rooms filled with objects and there is an edge between room1 and room2 iif 
     *      they are linked by a door
     */

    /*  2)  Each node of the graph is a room with all its objects  
     *      if a  forniture is inside the room we put it inside the graphEdge */
    Map<Room, RGraphEdge> graphRooms = new HashMap<Room, RGraphEdge>();

    List<RGraphEdge>   graphRoomsList = new ArrayList<RGraphEdge>();

    for (Room r : rooms)
    {
      RGraphEdge roomToFull = new RGraphEdge(r);


      for(HomePieceOfFurniture pieceOfForn : fornitures)
      {
        Point fornitPoint = new Point((int)pieceOfForn.getX() * 1000 , (int)pieceOfForn.getY() * 1000);

        Polygon roomShape = r.getPolygon1000xBigger();

        boolean isFornitureInsideRoom = roomShape.contains(fornitPoint) && (pieceOfForn.getLevel() == r.getLevel());

        if (isFornitureInsideRoom)
        {
          roomToFull.addPieceOfForniture(pieceOfForn);
        }
      }

      graphRoomsList.add(roomToFull);
      graphRooms.put(r, roomToFull);

    }

    /* 3) now we have the edges:  rooms filled of objects, we now need the links  */

    List<HomeDoorOrWindow> doors = new ArrayList<HomeDoorOrWindow>();
    for (HomePieceOfFurniture hpf : fornitures)
    {
      if (hpf instanceof HomeDoorOrWindow)
        doors.add(  (HomeDoorOrWindow) hpf);
    }

    // iterate on all the rooms and then intersect the bounding rectangles
    // if the (increased) bounding rect intersect, then we check wether there is a door
    Set<RGraphEdge> graphOfRe = new HashSet<RGraphEdge>();
    for (int i=0; i< rooms.size(); i++)
    {
      for (int j=i+1; j< rooms.size(); j++)
      {
        Room r1 = rooms.get(i), r2 = rooms.get(j);
        if (r1 != r2 &&  r1.intersectApprox(r2, 10))
        {

          for (HomeDoorOrWindow d : doors )
          {
            boolean areRoomsLinkedByDoor = areLinkedRoomsAndDoor(r1, r2, d);
            if ( areRoomsLinkedByDoor)
            {
              // there is a link bw the E with r1 and the E with r2
              //graphRooms.
              RGraphEdge e1 = graphRooms.get(r1);
              RGraphEdge e2 = graphRooms.get(r2);
              e1.addNeighbour(e2);
              e2.addNeighbour(e1);
              
              graphOfRe.add(e1);
              graphOfRe.add(e2);
            }
          }

        }
      }
    }

    return graphOfRe;

  }

  private boolean areLinkedRoomsAndDoor(Room r1, Room r2, HomeDoorOrWindow d)
  {

    Polygon pol1 = r1.getPolygon();
    Polygon pol2 = r2.getPolygon();

    float xc = d.getX();
    float yc = d.getY();
    float edge = d.getDepth() * 1.5f ;
    float upx = xc - edge / 2;
    float upy = yc - edge / 2;


    boolean inters1 = pol1.intersects(upx, upy, edge, edge);
    boolean inters2 = pol2.intersects(upx, upy, edge, edge);


    return inters1 && inters2;
  }
  
  /**
   * Builds the Reachability Tree
   * 
   * @param ev:  entry point of the graph: the node from which the visit begins
   * @param visited: the already visited nodes
   * @return the Reachability Tree
   *
   *  BT on a node with no neighbours returns a tree with just that node as leaf 
   *  BT on a node with neighbours returns a  tree with that node as root 
   *  and BT applied to all its sons except the ones already visited
   */
  
  protected RGraphEdge BT(RGraphEdge ev, Set<RGraphEdge> visited)
  {
    
       RGraphEdge tree = new RGraphEdge(ev.getRoom());
       
       // 1) get the neighbours
       
       List<RGraphEdge> sons = ev.getNeighbours();
       
       // 2) if there are no neighbours the functions returns
       
       if(sons == null)
           return tree;
       
       // 3)  otherwise there are neighbours, so we take off the already visited
       
       if (visited != null && visited.size() != 0) 
           removeSameRooms (sons, visited);
          
       if (sons.size() == 0)
           return tree;
       
       //4) we update the visited with the current node
       Set<RGraphEdge> visitedUpdated;
       if (visited != null)
            visitedUpdated = new HashSet<RGraphEdge>(visited);
       else
            visitedUpdated = new HashSet<RGraphEdge>();
       visitedUpdated.add(tree);  
       
       
       //5) we recursively apply the functions to all the sons
       
       List<RGraphEdge>  sonsUpdated = new ArrayList<RGraphEdge>();
       
       for (RGraphEdge son : sons)
       {
          sonsUpdated.add(BT(son, visitedUpdated));
       }
       
       tree.setNeighbours(sonsUpdated);
       
       return tree;
    
  }

  private void removeSameRooms(List<RGraphEdge> sons, Set<RGraphEdge> visited) {
        if (sons == null)
          return;
        if (sons.size() == 0)
          return;
        if (visited == null || visited.size() == 0)
          return;
        List<RGraphEdge> sons1 = new ArrayList<RGraphEdge>(sons);
        for (RGraphEdge v : visited)
           for (RGraphEdge s : sons1)
             if (areEquals(s, v))
                  sons.remove(s);
    
  }

  /*package*/ static boolean areEquals(RGraphEdge s, RGraphEdge v) {
    String n1 =  s.getRoom().getName();
    String n2 =  v.getRoom().getName();
    return n1.equals(n2);
  }


  public void displayTree() 
  {
    RGraphEdge root = this.makeReachTree();
    Graph g = Conversions.treeToGraph(root);
  }
  


}
