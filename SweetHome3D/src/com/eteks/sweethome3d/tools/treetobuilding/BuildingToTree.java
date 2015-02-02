package com.eteks.sweethome3d.tools.treetobuilding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import com.eteks.sweethome3d.tools.reachabletree.RGraphEdge;


public class BuildingToTree {
  
  
  /**
   * Convert a graph  http://graphstream-project.org
   * into an RGraphEdge
   * @param reachGraph the graph to convert
   * @return the converted graph  with 2 maps to have the corrispondency between nodes
   */
  @SuppressWarnings("unused")
  private GraphReachMap reachGraphToGraph(List<RGraphEdge> reachGraph)
  {
       
       Map<String, RGraphEdge> graphToReach = new HashMap<String, RGraphEdge>();
       Map<RGraphEdge, String> reachToGraph = new HashMap<RGraphEdge, String>();
       Graph graph = new SingleGraph("reach");
       
       for(RGraphEdge  rnode : reachGraph)
       {
            String nodeName = rnode.toStringShallow();
            graph.addNode(nodeName);
            graphToReach.put(nodeName, rnode);
            reachToGraph.put(rnode, nodeName);
            
            List<RGraphEdge> neighs = rnode.getNeighbours();
            
            for(RGraphEdge neigh : neighs)
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
