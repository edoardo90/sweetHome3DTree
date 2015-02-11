package com.eteks.sweethome3d.adaptive.treetobuilding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import com.eteks.sweethome3d.adaptive.reachabletree.SecurityGraphEdge;


public class BuildingToTree {
  
  
  /**
   * Convert a graph  http://graphstream-project.org
   * into an SecurityGraphEdge
   * @param reachGraph the graph to convert
   * @return the converted graph  with 2 maps to have the corrispondency between nodes
   */
  @SuppressWarnings("unused")
  private GraphReachMap reachGraphToGraph(List<SecurityGraphEdge> reachGraph)
  {
       
       Map<String, SecurityGraphEdge> graphToReach = new HashMap<String, SecurityGraphEdge>();
       Map<SecurityGraphEdge, String> reachToGraph = new HashMap<SecurityGraphEdge, String>();
       Graph graph = new SingleGraph("reach");
       
       for(SecurityGraphEdge  rnode : reachGraph)
       {
            String nodeName = rnode.toStringShallow();
            graph.addNode(nodeName);
            graphToReach.put(nodeName, rnode);
            reachToGraph.put(rnode, nodeName);
            
            List<SecurityGraphEdge> neighs = rnode.getNeighbours();
            
            for(SecurityGraphEdge neigh : neighs)
            {
               String neighName = neigh.toStringShallow();
               graph.addEdge(nodeName + neighName, nodeName ,neighName);
              
            }
       }
      GraphReachMap grm = new GraphReachMap();
      grm.setGraph(graph); 
      grm.setGraphToReach(graphToReach); 
      grm.setReachGraph(reachGraph);
      grm.setReachToGraph(reachToGraph);
      return grm;
  }
  
  
  
  
  
  
  
  
  
}
