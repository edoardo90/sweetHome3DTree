package com.eteks.sweethome3d.adaptive.security.buildingGraph.json;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.attributes.BuildingObjectAttribute;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSonGraph {

  private BuildingSecurityGraph segraph;

  public JSonGraph(BuildingSecurityGraph segraph) {
    this.segraph = segraph;
  }

  public String getContainmentStr()
  {
    Gson g = new GsonBuilder().setPrettyPrinting().create();
    
    Set<BuildingObjectContained> objects = segraph.getSetOfBuildingObjects();
    
    Set<BuildingObjSimple> objectSimples =  new HashSet<BuildingObjSimple>();
    
    for(BuildingObjectContained boc : objects)
    {
      BuildingObjSimple bos = new BuildingObjSimple(boc);
      objectSimples.add(bos);
    }
    
    
    
    String jsone = g.toJson(objectSimples);

    return jsone;
  }

  public static class BuildingObjSimple
  {
    private String id;
    private BuildingObjectType type;
    private Set<BuildingObjectAttribute> additionalAttributes;
    private Boolean connectable;
    private Set<String> sons;

    public BuildingObjSimple(BuildingObjectContained boc)
    {
      //BASIC
      String id = boc.getId();
      BuildingObjectType type = boc.getType();
      Set<BuildingObjectAttribute> additionalAttributes = 
          new  HashSet<BuildingObjectAttribute>(boc.getAttributes());
      Boolean connectable = boc.canConnect();
      
      //CONTAINED
      List<String> containedLst = boc.getObjectConainedStr();
      
      
      this.id = id;
      this.type = type;
      this.additionalAttributes = additionalAttributes;
      this.connectable = connectable;
      this.sons = new HashSet<String>(containedLst);
      
    }
    

  }


}
