package com.eteks.sweethome3d.tools.treetobuilding;

import static org.graphstream.ui.graphicGraph.GraphPosLengthUtils.nodePosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerPipe;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomeDoorOrWindow;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;

import com.eteks.sweethome3d.model.Room;


public class GraphDisplayer {
  

  private Graph graph = null;
  private Home home;
  public GraphDisplayer()
  {
  }
  
  public GraphDisplayer(Graph graph, Home home)
  {
    this.home = home;
    this.graph = graph;
    

  }


  public void click(boolean visibleGraph) 
  {
    Graph myGraph;
     if(graph == null)
     {
        myGraph = new SingleGraph("my click");
        this.addStubNodes(myGraph);
     }
     else
     {
       myGraph = graph;
     }
     
     
    Viewer viewer ;

    viewer =  visibleGraph? myGraph.display() : new Viewer(myGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
    viewer.enableAutoLayout();
    viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);


    ViewerPipe fromViewer = viewer.newViewerPipe();
    fromViewer.addSink(myGraph);

    
    
    Double oldx=null; int nequal = 0; int nd=0;
    boolean needToUpdate = true;
    
    while(true) {
      
 
      for (Node node : graph) {
        node.addAttribute("ui.label", node.getId());
      }
      
      fromViewer.pump(); 
      sleep(200);

      for (Node node : graph) {
        node.addAttribute("ui.label", node.getId());
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
        updateHome(myGraph);
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
 

  public void updateHome(Graph myGraph) {
    
    System.out.println("update home");
    
    //cooridor needs a door
    HomeDoorOrWindow door = null;
    
    //TODO : ask sweetHome people how to add door runtime by code
    for(HomePieceOfFurniture p : home.getFurniture())
    {
        if(p instanceof HomeDoorOrWindow)
        {
           door = new HomeDoorOrWindow( (HomeDoorOrWindow) p);
           door = door.clone();
          break;
        }
        
    }
    
    
    Map<String, double []> nodesPositions = Conversions.getAllPositions(myGraph);
    RoomsAndCorridors rac = buildBuildingFromGraph(myGraph, nodesPositions, door);
    
    
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
        //home.deletePieceOfFurniture(p);
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
  
  
}

