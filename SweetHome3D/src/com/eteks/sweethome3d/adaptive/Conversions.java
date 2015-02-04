package com.eteks.sweethome3d.adaptive;

import static org.graphstream.ui.graphicGraph.GraphPosLengthUtils.nodePosition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import com.eteks.sweethome3d.adaptive.reachabletree.RGraphEdge;

public class Conversions {
  
    public static Map<String, double []> getAllPositions(Graph myGraph) {
    
    Map<String, double []> nodesPositions = new HashMap<String, double[]>();
    
    Double xMin = null;
    Double xMax = null;
    Double yMax = null;
    
    /* move cartesian axes in 0,0  like in sweetHome3D */
    for(Node n : myGraph)
    {
      
       double [] positions = nodePosition(n);
       double x = positions[0],  y = positions[1];
      
       if(xMin == null ||  x < xMin)
         xMin = x;
       
       if(xMax == null ||  x > xMax)
         xMax = x;
       
       if(yMax == null || y > yMax)
         yMax = y;
       
    }
    
    Double newxMax = null, newyMax=null;
    
    for (Node n : myGraph)
    {
      double [] positions = nodePosition(n);
      double x = positions[0];
      double y = positions[1];
      
      x = Math.abs( x - xMin);
      y = Math.abs(y - yMax);
      
      if (newxMax == null || x > newxMax)
        newxMax = x;
      if (newyMax == null || y > newyMax)
        newyMax = y;
      
      
      positions[0] = x;
      positions[1] = y ;
      
      nodesPositions.put(n.getId(), positions);
     
    }
    
    for(String s : nodesPositions.keySet())
    {
      
      double [] pos = nodesPositions.get(s);
      double x = pos[0], y = pos[1];
      if(newxMax != 0)
        x = (x / newxMax);
      if(newyMax != 0)
        y = (y / newyMax);
      
      if( ! (x <= 1 && x >= 0))
        System.out.println("bad x !");
      if (! (y <= 1 && y >= 0))
         System.out.println(" bad y ! ");
      pos[0]= x;
      pos[1]=y;
      nodesPositions.put(s, pos);
    }
    
    return nodesPositions;
   }
   
   private static Graph treeToGraph(RGraphEdge root, Graph graph)
   {
     if(root == null || graph == null)
       return null;
     
     Graph g = graph;
     
     String name = root.toStringShallow();
     try  {         graph.addNode(name);   }   catch (IdAlreadyInUseException e)    {     }
     List<RGraphEdge> sons = root.getNeighbours();
     
     //if root is a LEAF:
     if (sons == null || sons.size() == 0)
     {
       return graph;
     }
     else
     {
       for(RGraphEdge rge : sons)
       {
         String nameSon = rge.toStringShallow();
         
         try  {   graph.addNode(nameSon);   }   catch (IdAlreadyInUseException e)    {     }
         
         try  {   graph.addEdge(nameSon + name, nameSon, name); }   catch(Exception e) {}
         
         g = treeToGraph(rge, g);
         
       }
     }
     
     return g;
     
   }
   
   public static Graph treeToGraph(RGraphEdge root)
   {
     
     Graph g  = new SingleGraph(root.toStringShallow());
     
     return treeToGraph(root, g);
     
   }
   


}
