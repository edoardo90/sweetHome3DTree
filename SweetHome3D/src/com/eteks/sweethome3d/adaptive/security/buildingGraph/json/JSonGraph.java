package com.eteks.sweethome3d.adaptive.security.buildingGraph.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingRoomNode;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.CyberLinkEdge;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.IdObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.viewcontroller.HomeController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

public class JSonGraph {

  private BuildingSecurityGraph segraph;
  private HomeController controller;
  public JSonGraph(BuildingSecurityGraph segraph, HomeController controller) {
    this.segraph = segraph;
    this.controller = controller;
  }

  public String getContainmentStr()
  {
    Gson g = new GsonBuilder().setPrettyPrinting().create();
    
    Set<BuildingObjectContained> objects = segraph.getSetOfBuildingObjects();
    
    Set<BuildingObjSimple> objectSimples =  new HashSet<BuildingObjSimple>();
    
    for(BuildingObjectContained boc : objects)
    {
      BuildingRoomNode broom = segraph.getBuildingRoomFromObj(new IdObject(boc.getId()));
      
      BuildingObjSimple bos = new BuildingObjSimple(boc, broom.getId());
      objectSimples.add(bos);
    }
    
    
    
    String jsone = g.toJson(objectSimples);

    return jsone;
  }
  
  public void simulateNewContainment() {
    Gson g = new Gson();
    
    String sons = this.getSimulateSons();
    
    HashSet<BuildingObjSimple> objectSimpless = g.fromJson(sons, HashSet.class);
    objectSimpless.contains(null);
    Map<String, String> objectsPosition = new HashMap<String, String>();
    for(Object boss : objectSimpless)
    {
      if(boss instanceof LinkedTreeMap)
      {
        LinkedTreeMap bosm = (LinkedTreeMap) boss;
        List<Object> keyset = new ArrayList(bosm.values());
        String id =(String)      keyset.get(0);
        String idRoom = (String) keyset.get(2);
        objectsPosition.put(id, idRoom);
      }
    }
    
    for(Entry<String, String> entry : objectsPosition.entrySet())
    {
      String objectID = entry.getKey();
      String objectIDRoom = entry.getValue();
      //segraph.moveObject(agentID, agentIDRoom);
      controller.moveObject(objectID, objectIDRoom);
    }
   
  }
  
  private String getSimulateSons()
  {
    BuildingObjSimple bos0 = new BuildingObjSimple("Pc-01", "kitchen");
    BuildingObjSimple bos1 = new BuildingObjSimple("Printer-01", "kitchen");
    BuildingObjSimple bos2 = new BuildingObjSimple("Printer-02", "livingRoom");
    Set<BuildingObjSimple> boss = new HashSet<BuildingObjSimple>();
    
    boss.add(bos0);
    boss.add(bos1);
    boss.add(bos2);
    
    Gson g = new GsonBuilder().setPrettyPrinting().create();
    return g.toJson(boss);
    
  }
  
  public void simulateNewLinks()
  {
    String links = getSimulateLinks();
    Gson g = new Gson();
    HashSet<CyberLinkAnalysis> analysisLinks = g.fromJson(links, HashSet.class);
    
    for(Object link : analysisLinks)
    {
      if(link instanceof LinkedTreeMap)
      {
        LinkedTreeMap bosm = (LinkedTreeMap) link;
        List<Object> keyset = new ArrayList(bosm.values());
        String label =(String)      keyset.get(0);
        String source = (String) keyset.get(1);
        ArrayList<String> targets = (ArrayList<String>) keyset.get(2);
        
        List<CyberLinkSimple> linksSimple = getCybersSimple(label, source, targets);
        System.out.println("Cyber Links Simple:" + linksSimple);
      }
    }
  }
  
  private List<CyberLinkSimple> getCybersSimple(String label, String source, List<String> targets)
  {
    List<CyberLinkSimple> cyberLinksSimple = new ArrayList<CyberLinkSimple>();
    for(String target : targets)
    {
      CyberLinkSimple cyberSimple = new CyberLinkSimple(source, target, label);
      cyberLinksSimple.add(cyberSimple);
    }
    return cyberLinksSimple;
  }
  
  private String getSimulateLinks()
  {
    CyberLinkAnalysis ca1 = new CyberLinkAnalysis("wifi", "bob", "printer-1", "cctv_kitch");
    CyberLinkAnalysis ca2 = new CyberLinkAnalysis("wifi", "bob", "VM3", "VM4");
    Set<CyberLinkAnalysis> cans = new HashSet<CyberLinkAnalysis>();
    cans.add(ca1);
    cans.add(ca2);
    
    Gson g = new GsonBuilder().setPrettyPrinting().create();
    return g.toJson(cans);
  }
  
  
  public String getLinks() {
    Gson g = new GsonBuilder().setPrettyPrinting().create();
    BuildingSecurityGraph segraph = BuildingSecurityGraph.getInstance();
    Set<CyberLinkEdge> cyberLinks = segraph.getCyberLinks();
    Set<CyberLinkSimple> cyberLinksSimple = new HashSet<CyberLinkSimple>();
    for(CyberLinkEdge cl : cyberLinks)
    {
      cyberLinksSimple.add(new CyberLinkSimple(cl.getIdObject1(), cl.getIdObject2(), cl.getName()));
    }
    return g.toJson(cyberLinksSimple);
  }
  
  public static class CyberLinkAnalysis
  {
    private String label = "";
    private String source = "";
    private Set<String> targets = new HashSet<String>();
    
    public CyberLinkAnalysis(String label, String source, String ... targets)
    {
      this.label = label;
      this.source = source;
      for(String target : targets)
      {
        this.targets.add(target);
      }
    }
    
    public String getLabel() {
      return label;
    }
    public void setLabel(String label) {
      this.label = label;
    }
    public String getSource() {
      return source;
    }
    public void setSource(String source) {
      this.source = source;
    }
    public Set<String> getTargets() {
      return targets;
    }
    public void setTargets(Set<String> targets) {
      this.targets = targets;
    }
  }
  
  
  public static class CyberLinkSimple
  {
    private String id1;
    private String id2;
    private String nameLink;
    
    public CyberLinkSimple(String id1, String id2, String name)
    {
      this.id1 = id1;
      this.id2 = id2;
      this.nameLink = name;
    }
    
    public String getId1() {
      return id1;
    }
    public void setId1(String id1) {
      this.id1 = id1;
    }
    public String getId2() {
      return id2;
    }
    public void setId2(String id2) {
      this.id2 = id2;
    }
    public String getNameLink() {
      return nameLink;
    }
    public void setNameLink(String nameLink) {
      this.nameLink = nameLink;
    }
    
    public String toString()
    {
      return "\n" + this.getId1() + " <=:=:= " + this.getNameLink() + " :=:=>" + this.getId2();  
    }
    
  }
  
  public static class BuildingObjSimple
  {
    private String id;
    private String type;
    private String roomId;
    private Set<String> additionalAttributes;
    private String connectable;
    private Set<String> sons;
    
    public BuildingObjSimple(String id, String roomId)
    {
      this.id = id;
      this.type = "";
      this.roomId = roomId;
      this.additionalAttributes = new HashSet<String>();
      this.connectable = "";
        
    }
    
    public BuildingObjSimple(BuildingObjectContained boc, String bRoomId)
    {
      //BASIC
      String id = boc.getId();
      BuildingObjectType type = boc.getType();
      Set<String> additionalAttributes = boc.getAttributesStr(); 
      String  connectable = String.valueOf( boc.canConnect());
      
      //CONTAINED
      List<String> containedLst = boc.getObjectConainedIDStr();
      
      
      this.setId(id);
      this.setRoomId(bRoomId);
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

    public String getRoomId() {
      return roomId;
    }

    public void setRoomId(String roomId) {
      this.roomId = roomId;
    }
    

  }
}
