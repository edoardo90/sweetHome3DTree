package com.eteks.sweethome3d.tools.treetobuilding;

import static org.graphstream.ui.graphicGraph.GraphPosLengthUtils.nodePosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerPipe;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomeDoorOrWindow;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.tools.Conversions;
import com.eteks.sweethome3d.tools.reachabletree.NullGraphExcepion;



public class GraphDisplayer {

  private static GraphDisplayer graphDisplayer = null;
  private Graph graphGeneral = null;
  private Graph graphTree = null;
  private Home home;
  private Viewer generalGraphViewer = null;
  private Viewer treeGraphViewer = null;


  public enum KindOfGraph { GRAPH, TREE ; }

  private GraphDisplayer(Home home)
  {
    this.home = home;

  }

  public void addGraph(KindOfGraph kind, Graph graph)
  {
    if(kind == KindOfGraph.GRAPH)
    {
      this.graphGeneral = graph;
    }
    else
    {
      this.graphTree    = graph;
    }
  }

  public static synchronized GraphDisplayer getDisplayer(Graph graph, Home home)
  {
    if(GraphDisplayer.graphDisplayer == null)
      graphDisplayer = new GraphDisplayer(home);

    return graphDisplayer;
  }

  public void showAndMonitor(final boolean visibleGraph, final KindOfGraph kind) throws NullGraphExcepion 
  {
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    executorService.execute(new Runnable() {

      public void run() {
        try {
          showAndMonitorThreaded(visibleGraph, kind);
        } catch (NullGraphExcepion ex) {
          ex.printStackTrace();
        } 
      }


    });

    executorService.shutdown();

  }

  private void showAndMonitorThreaded(boolean visibleGraph, KindOfGraph kind ) throws NullGraphExcepion
  {
    Graph myGraph;


    if(kind == KindOfGraph.GRAPH)
    {
      if(graphGeneral == null)
      {
        throw new NullGraphExcepion("null graph!");
      }

      myGraph = graphGeneral;

    }
    else
    {
      if(graphGeneral == null)
      {
        throw new NullGraphExcepion("null graph!");
      }
      myGraph = graphTree;
    }


    Viewer viewer;

    if(kind == KindOfGraph.GRAPH && this.generalGraphViewer != null)
    {
      viewer = this.generalGraphViewer;
    }
    else if( kind == KindOfGraph.TREE && this.treeGraphViewer != null)
    {
      viewer = this.treeGraphViewer;
    }
    else
    {
      viewer =  visibleGraph? myGraph.display() : new Viewer(myGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
      viewer.enableAutoLayout();
      viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);

      if(kind == KindOfGraph.TREE)
        this.treeGraphViewer = viewer;
      else
        this.generalGraphViewer = viewer;

    }


    ViewerPipe fromViewer = viewer.newViewerPipe();
    fromViewer.addSink(myGraph);

    Double oldx=null; int nequal = 0; 
    boolean needToUpdate = true;

    while(true) {

      if ( ! myGraph.getId().equals("home graph"))
      {
        int x = 0;
        x++;
      }


      fromViewer.pump(); 
      sleep(200);

      for (Node node : myGraph) {
        String label = "";
        if(kind == KindOfGraph.TREE)
        {
          label =  "" +  node.getId().charAt(node.getId().length() - 1);

        }
        else
        {
          label = ""+   node.getId();
        }
        node.addAttribute("ui.label", label);

      }

      Node node = myGraph.getNode("A");
      if (node == null)
        continue;
      double pos[] = nodePosition(node);

      if(oldx == null || oldx != pos[0])
      {
        needToUpdate = true;
      }
      else
      {
        nequal++;
      }
      if(nequal == 5)
      {
        //updateHome(myGraph);
      }

      if( needToUpdate )
      {
        nequal =0;

        for(Node n : myGraph)
        {
          double posi[] = nodePosition(n);
          System.out.println(n.getId() +  ": X = " + posi[0] + "Y = " + posi[1]);
        }
        needToUpdate = false;

      }
      oldx = pos[0];
    }
  }

  public void updateHomeTreeWay(Graph myGraph)
  {
    updateHome(myGraph, true);

  }


  public void updateHomeCoverGraphWay(Graph myGraph) {
    updateHome(myGraph, false);
  }

  public void updateHome(Graph myGraph, boolean isTree)
  {

    System.out.println(" now  I  update home in beasty way");

    //cooridor needs a door
    HomeDoorOrWindow door = getDoor();

    Map<String, double []> nodesPositions = Conversions.getAllPositions(myGraph);


    RoomsAndCorridors rac ;

    if( isTree)
    {
      rac = buildBuildingFromTree(myGraph, nodesPositions, door);
    }
    else
    {
      rac = buildBuildingFromGraph(myGraph, nodesPositions, door);
    }


    List<Room> rooms = rac.getRooms();
    List<Corridor> corridors = rac.getCorridors();

    for (Room r : home.getRooms())
    {
      home.deleteRoom(r);

    }

    for( HomePieceOfFurniture p : home.getFurniture())
    {
      if(p instanceof HomeDoorOrWindow)
      {
        HomeDoorOrWindow hd  =  (HomeDoorOrWindow) p;
        if(hd.equals(door))
        {
          continue;
        }
      }

    }


    sleep(200);

    for(Room r : rooms)
    {

      home.addRoom(r);
    }

    sleep(100);

    for(Corridor c : corridors)
    {
      if(true)
      {
        Room corridorShape = c.getCorridorShape();
        home.addRoom(corridorShape);
        // home.addPieceOfFurniture(c.getFirstDoor());
        // home.addPieceOfFurniture(c.getSecondDoor());
      }
    }


  }

  /*
   * 
   *   -------------------------------
   *   |   C  |   D     | E  | F   | G |
   *   |______|_________|____|_____|___| 
   *   |       A        |      B       |
   */

  private RoomsAndCorridors buildBuildingFromTree(Graph myGraph, Map<String, double []> nodesPositions,
                                                  HomeDoorOrWindow door) {

    List<Corridor> cors  = new ArrayList<Corridor>();
    List<Room> rooms = new ArrayList<Room>();

    Node root = myGraph.getNode("A");
    Map<Node, NodeRoom> weightedTree = getWeightedTree(root);
    
    for( Entry<Node, NodeRoom> nodeAndRoom :  weightedTree.entrySet())
    {
      //create a room starting from NodeRoom information
      NodeRoom nr = nodeAndRoom.getValue();
      Room r = getRoomFromNodeRoom(nr); 
      rooms.add(r);
    }

    RoomsAndCorridors rac = new RoomsAndCorridors(cors, rooms);
    return rac;
  }

  private Room getRoomFromNodeRoom(NodeRoom nr) {

    int y = nr.level;
    int width = nr.numberOfDesc;  // should also be function of objects inside...
    int x = nr.leftDisplacement;
    int roomHeight = 3;   //should be also function of the number of objects inside...
    int fatherEnd = nr.fatherFinalEnd;
    int conversion = 100;
    /*  +----x--->
     *  |        2          3
     *  y
     *  |
     *  V        1          4
     *
     */
    
    float x1 = x * conversion;
    float y1 = fatherEnd * conversion;
    
    float x3 = x1 + width * conversion;
    float y3 = y1 + roomHeight * conversion;

    float x2 = x1;
    float y2 = y3;

    float x4 = x3; 
    float y4 = y1;

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
    room.setName(nr.id);
    return room;
  }

  private Map<Node, NodeRoom> getWeightedTree(Node myTreeRoot)
  {
    return this.getWeightedTreeAcc(myTreeRoot, null, 0, 0, 0);
  }
  /**
   * Associate each node with the corresponding room
   * Each room is as width  as the number of discendent of the corresponding node
   * 
   * @param myTreeRoot root node
   * @param father  father of node
   * @param level   level
   * @param fatherLeftDisplacement   propagated because the function recursively fills the map
   * @param fatherFinalEnd   the same as width
   * @return a map in which key is the node and value is the associated room
   */
  private Map<Node, NodeRoom> getWeightedTreeAcc(Node myTreeRoot, Node father, int level, int fatherLeftDisplacement, int fatherFinalEnd)
  {
    Map<Node, NodeRoom> mapDecorTree = new HashMap<Node, GraphDisplayer.NodeRoom>();
    NodeRoom nr = new NodeRoom();
    nr.level = level;
    nr.leftDisplacement = fatherLeftDisplacement;
    nr.id = myTreeRoot.getId();
    nr.fatherFinalEnd = fatherFinalEnd;
    nr.numberOfDesc = this.numberOfDisc(myTreeRoot, father);
    mapDecorTree.put(myTreeRoot, nr);

    Iterator<Node> neighboursIt = myTreeRoot.getNeighborNodeIterator();
    int w = 0; // w is the displacement of each son 
    while(neighboursIt.hasNext())
    {
      Node neigh = neighboursIt.next();
      //skip father
      if(differentNodes(neigh, father))
      {
        int heightOfRoom = 3;  /* height should consider the number of objcets, but from just the tree we
                                * don't know the objects contained  */
        Map<Node, NodeRoom> nodeDecorated = getWeightedTreeAcc(neigh, myTreeRoot, level + 1, w + fatherLeftDisplacement, fatherFinalEnd + heightOfRoom);
        
        int numberOfDesc = nodeDecorated.get(neigh).numberOfDesc; //number of discendend of the just added son
        /**
         *  A
         *  C                B
         *  D     E          F    G   H
         *  0     1          2    3   4
         * 
         * A.x  = 0 + 0
         * C.x  =  0 + 0
         * B.x   have to be 2  in order to left space for C that takes 2 cells, thats why we
         * propagate the left displacement in the recursive call
         * D.x = 0 + 0   (w = 0, fatherLeftDisplacement = A.x = 0) 
         * E.x = 0 + 1  (w = 1, fatherLeftDisplacement = A.x = 0) 
         * F.x = 2      (w = 0, fatherLeftDisplacement = B.x  = 2)
         * G.x = 3      (w = 1, fatherLeftDisplacement = B.x  = 2)
         * H.x = 4      (w = 2, fatherLeftDisplacement = B.x  = 2)
       */
        for(Entry<Node, NodeRoom> nodeDec : nodeDecorated.entrySet())
        {
          mapDecorTree.put(nodeDec.getKey(), nodeDec.getValue());
        }

        w += numberOfDesc;
      }

    }

    return mapDecorTree;
  }

  /**
   * returns 1 for leafs
   * returns  the number of discendent for inner nodes
   * @param n node
   * @param fatherOfN  father
   * @return the number of discendent of the passed node
   */
  private int numberOfDisc(Node n, Node father)
  {

    Iterator<Node> neighboursIt = n.getNeighborNodeIterator();
    List<Node> neighBours = new ArrayList<Node>();
    int sum = 0;
    while(neighboursIt.hasNext())
    {
      neighBours.add(neighboursIt.next());
    }
    
    //the list comprends the father because we use graph structure also for trees
    
    // leaf:   neigh:   Father  ->  1
    // non-leaf with one desc :   Father, Desc1  ->  1
    // non-leaf with two desc :    Fath ,  D1, D2  -> 2
    // non-leaf with three desc :    Fath ,  D1, D2, D3 -> 3
    
    //leaf
    if (neighBours.size() == 1  && ! differentNodes(neighBours.get(0), father))
    {
      return 1;
    }
    
    for(Node neigh : neighBours)
    {
      //skip the father
      if( differentNodes(neigh, father))
      {
        sum = sum + numberOfDisc(neigh, n);
      }
    }

    return sum;
  }
  
  private boolean differentNodes(Node n1, Node n2)
  {
    if (n1 == null || n2 == null)
      return true;
    return !  n1.getId().equals(n2.getId());
  }

  private HomeDoorOrWindow getDoor()
  {
    HomeDoorOrWindow door = null;
    //TODO : runtime door - pieceofforinuture
    for(HomePieceOfFurniture p : home.getFurniture())
    {

      if(p instanceof HomeDoorOrWindow)
      {
        door = new HomeDoorOrWindow( (HomeDoorOrWindow) p);
        door = door.clone();
        break;
      }

    }
    return door;
  }

/**
 * Returns rooms and corridors that are derived from a graph
 * @param graph
 * @param nodesPositions
 * @param door
 * @return
 */
  public  RoomsAndCorridors buildBuildingFromGraph(Graph graph, Map<String, double []> nodesPositions, HomeDoorOrWindow door )
  {
    List<Room> rooms = new ArrayList<Room>();
    List<Corridor> corridors = new ArrayList<Corridor>();

    // for each node i draw a room, its size will be proportional to the number of neighbours in the various directions
    for (Node n : graph)
    {
      GraphicInfoNode graphicNode = getGraphicInfoNode(n.getId(), graph, nodesPositions);
      Room r = graphicNode.getRoom();
      rooms.add(r);

      Iterator<Node> it = n.getNeighborNodeIterator();
      while(it.hasNext())
      {
        Node neigh = it.next();

        Corridor cc1 = new Corridor(n.getId(), neigh.getId());
        Corridor cc2 = new Corridor(neigh.getId(), n.getId());
        if( (!corridors.contains(cc2))  &&  (! corridors.contains(cc1))  ) //if there is no already link with neigh
        {
          GraphicInfoNode  graphInfoNeigh = getGraphicInfoNode(neigh.getId(), graph, nodesPositions);
          Direction directionOfNeighbour  = graphicNode.getDirectionOfNeighbour(graphInfoNeigh);
          Corridor c = new Corridor(graphInfoNeigh, graphicNode, directionOfNeighbour, door);
          corridors.add(c);
        }
      }

    }


    return  new RoomsAndCorridors(corridors, rooms);
  }




  private GraphicInfoNode getGraphicInfoNode(String nodeID, Graph graph, Map<String, double []> nodesPositions)
  {

    if(nodesPositions == null)
      throw new IllegalArgumentException();
    Node baseNode = graph.getNode(nodeID);
    GraphicInfoNode gin = new GraphicInfoNode(baseNode, nodesPositions);
    return gin;

  }



  private void sleep(int ms) { try {      Thread.sleep(ms);} catch (InterruptedException e) {     } }




  private void addStubNodes(Graph myGraph)
  {
    myGraph.addNode("A");
    myGraph.addNode("B");
    myGraph.addNode("C");
    myGraph.addNode("D");
    myGraph.addNode("E");


    myGraph.addEdge("AB", "A", "B");
    myGraph.addEdge("AC", "A", "C");
    myGraph.addEdge("AD", "A", "D");
    myGraph.addEdge("AE", "A", "E");
    myGraph.addEdge("EB", "E", "B");
  }

  private class NodeRoom
  {
    public int fatherFinalEnd;
    public int level;
    public int leftDisplacement;
    public int numberOfDesc;
    public String id;
    
    public String toString ()
    {
      return "ID:" + this.id + "nod:" + numberOfDesc;
    }
    
  }


}

