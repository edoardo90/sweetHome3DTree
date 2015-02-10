package com.eteks.sweethome3d.adaptive.security.ifcSecurity;

import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.MultiNode;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildinLinkWallWithDoor;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingLinkEdge;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingLinkWall;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingSecurityGraph;


public class GraphClean {

  private Graph homeGraph;

  private List<BuildingLinkEdge> links;

  public GraphClean(BuildingSecurityGraph securityGraph)
  {
    this.homeGraph = new MultiGraph("my");
    links = securityGraph.getLinkEdgeList();
  }

  public void show()
  {
    putlabels();
    homeGraph.display();
  }



  public void populateGraph()
  {

    for(BuildingLinkEdge link :  links)
    {
      String room1 = link.getFirstRoom();
      String room2 = link.getSecondRoom();
      String idLink ;
      idLink= room1 + link.getId() + room2;
      if (link instanceof BuildinLinkWallWithDoor) {
        BuildinLinkWallWithDoor linkDoor = (BuildinLinkWallWithDoor)link;
        idLink = "D" + idLink;
       
      }
      else  if (link instanceof BuildingLinkWall) {
        BuildingLinkWall linkWall = (BuildingLinkWall)link;
        idLink = "W" + idLink;
       
      }
      

      this.addEdgeToGraph(idLink, room1, room2);

    }

  }

  private void putlabels()
  {
    for (Node node : this.homeGraph) 
    {
      String  label =    node.getId();
      node.addAttribute("ui.label", label);
    }
    
    for (Edge edge : this.homeGraph.getEdgeSet())
    {
      String label = edge.getId();
      
      System.out.println(label.charAt(0));
      if(label.charAt(0) == 'D')
      {
        edge.setAttribute("ui.label","DOOR");
      }
      else
      {
        edge.setAttribute("ui.label", "_____________WALL");
        
      }
      
    }
    
  }

  private void addEdgeToGraph(String edgeId, String n1, String n2)
  {
    this.addNodeToGraph(n1);
    this.addNodeToGraph(n2);
    if(homeGraph.getEdge(edgeId)== null)
    {
      homeGraph.addEdge(edgeId, n1, n2);
    }
    else
    {
      System.out.println("edge not added " + edgeId + " cause already present");
    }
  }

  private void addNodeToGraph(String nodeId)
  {
    if(homeGraph.getNode(nodeId)== null)
      homeGraph.addNode(nodeId);
  }


  private void toyExample(){
    MultiNode A=homeGraph.addNode("A");
    MultiNode B=homeGraph.addNode("B");

    homeGraph.addEdge("AB1","A","B");
    homeGraph.addEdge("AB2","A","B");

    SpriteManager sman = new SpriteManager(homeGraph);
    Sprite s = sman.addSprite("S1");
    s.attachToEdge("AB1");
  }



}
