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
    private String type;
    private Set<String> additionalAttributes;
    private String connectable;
    private Set<String> sons;

    public BuildingObjSimple(BuildingObjectContained boc)
    {
      //BASIC
      String id = boc.getId();
      BuildingObjectType type = boc.getType();
      Set<String> additionalAttributes = boc.getAttributesStr(); 
      String  connectable = String.valueOf( boc.canConnect());
      
      //CONTAINED
      List<String> containedLst = boc.getObjectConainedIDStr();
      
      
      this.setId(id);
      this.setType("" + type);
      this.setAdditionalAttributes(additionalAttributes);
      this.setConnectable(connectable);
      this.setSons(new HashSet<String>(containedLst));
      
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public Set<String> getAdditionalAttributes() {
      return additionalAttributes;
    }

    public void setAdditionalAttributes(Set<String> additionalAttributes) {
      this.additionalAttributes = additionalAttributes;
    }

    public String getConnectable() {
      return connectable;
    }

    public void setConnectable(String connectable) {
      this.connectable = connectable;
    }

    public Set<String> getSons() {
      return sons;
    }

    public void setSons(Set<String> sons) {
      this.sons = sons;
    }
    

  }


}
