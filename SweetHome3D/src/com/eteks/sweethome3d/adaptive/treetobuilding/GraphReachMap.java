package com.eteks.sweethome3d.adaptive.treetobuilding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.graphstream.graph.Graph;

import com.eteks.sweethome3d.adaptive.reachabletree.SecurityGraphEdge;

public class GraphReachMap {
  private Map<String, SecurityGraphEdge> graphToReach = new HashMap<String, SecurityGraphEdge>();
  private Map<SecurityGraphEdge, String> reachToGraph = new HashMap<SecurityGraphEdge, String>();
  private Graph graph;
  private List<SecurityGraphEdge> reachGraph;
  public Map<String, SecurityGraphEdge> getGraphToReach() {
    return graphToReach;
  }
  public void setGraphToReach(Map<String, SecurityGraphEdge> graphToReach) {
    this.graphToReach = graphToReach;
  }
  public Graph getGraph() {
    return graph;
  }
  public void setGraph(Graph graph) {
    this.graph = graph;
  }
  public Map<SecurityGraphEdge, String> getReachToGraph() {
    return reachToGraph;
  }
  public void setReachToGraph(Map<SecurityGraphEdge, String> reachToGraph) {
    this.reachToGraph = reachToGraph;
  }
  public List<SecurityGraphEdge> getReachGraph() {
    return reachGraph;
  }
  public void setReachGraph(List<SecurityGraphEdge> reachGraph) {
    this.reachGraph = reachGraph;
  }

}
