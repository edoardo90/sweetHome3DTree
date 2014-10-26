package com.eteks.sweethome3d.tools.treetobuilding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.graphstream.graph.Graph;

import com.eteks.sweethome3d.tools.reachabletree.RGraphEdge;

public class GraphReachMap {
  private Map<String, RGraphEdge> graphToReach = new HashMap<String, RGraphEdge>();
  private Map<RGraphEdge, String> reachToGraph = new HashMap<RGraphEdge, String>();
  private Graph graph;
  private List<RGraphEdge> reachGraph;
  public Map<String, RGraphEdge> getGraphToReach() {
    return graphToReach;
  }
  public void setGraphToReach(Map<String, RGraphEdge> graphToReach) {
    this.graphToReach = graphToReach;
  }
  public Graph getGraph() {
    return graph;
  }
  public void setGraph(Graph graph) {
    this.graph = graph;
  }
  public Map<RGraphEdge, String> getReachToGraph() {
    return reachToGraph;
  }
  public void setReachToGraph(Map<RGraphEdge, String> reachToGraph) {
    this.reachToGraph = reachToGraph;
  }
  public List<RGraphEdge> getReachGraph() {
    return reachGraph;
  }
  public void setReachGraph(List<RGraphEdge> reachGraph) {
    this.reachGraph = reachGraph;
  }

}
