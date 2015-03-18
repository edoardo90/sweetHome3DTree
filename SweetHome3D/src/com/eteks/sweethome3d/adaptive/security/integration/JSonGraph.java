package com.eteks.sweethome3d.adaptive.security.integration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.event.ListSelectionEvent;

import com.eteks.sweethome3d.adaptive.security.assets.Asset;
import com.eteks.sweethome3d.adaptive.security.assets.AssetType;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingRoomNode;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.CyberLinkEdge;
import com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper.IdObject;
import com.eteks.sweethome3d.viewcontroller.HomeController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

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
    
    Set<Asset> objects = segraph.getSetOfBuildingObjects();
    
    Set<BuildingObjSimple> objectSimples =  new HashSet<BuildingObjSimple>();
    
    for(Asset boc : objects)
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
      segraph.moveObject(objectID, objectIDRoom);
      controller.moveObject(objectID, objectIDRoom);
    }
   
  }
  
  private String getSimulateSons()
  {
    BuildingObjSimple bos0 = new BuildingObjSimple("bob", "vure"); //safe-room
    BuildingObjSimple bos1 = new BuildingObjSimple("pc-cb", "p1an"); //kitchen
    Set<BuildingObjSimple> boss = new HashSet<BuildingObjSimple>();
    
    boss.add(bos0);
    boss.add(bos1);
    
    Gson g = new GsonBuilder().setPrettyPrinting().create();
    return g.toJson(boss);
    
  }
  
  private String getSimulateLinks()
  {
    CyberLinkAnalysis ca1 = new CyberLinkAnalysis("wifi", "bob", "pc-cb", "pc-0");
    CyberLinkAnalysis ca2 = new CyberLinkAnalysis("eth0", "bob", "light-0", "cctv-0");
    Set<CyberLinkAnalysis> cans = new HashSet<CyberLinkAnalysis>();
    cans.add(ca1);
    cans.add(ca2);
    
    Gson g = new GsonBuilder().setPrettyPrinting().create();
    return g.toJson(cans);
  }
  
  
  
  
  public void simulateNewLinks()
  {
    String links = getSimulateLinks();
    Gson g = new Gson();
    HashSet<CyberLinkAnalysis> analysisLinks = g.fromJson(links, HashSet.class);
    
    List<CyberLinkSimple>  allTotalSimplesLinks = new ArrayList<CyberLinkSimple>();
    
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
        allTotalSimplesLinks.addAll(linksSimple);
      }
    }
    System.out.println("Cyber Links Simple:" + allTotalSimplesLinks);
    this.controller.clearLinks();
    for(CyberLinkSimple cls : allTotalSimplesLinks)
    {
      String id1 = cls.getId1();
      String id2 = cls.getId2();
      String name = cls.getNameLink();
      
      controller.addCyberLink(id1, id2, name);
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
  

  public String getLinksBigraphishFormat()
  {
    
    Gson g = new GsonBuilder().setPrettyPrinting().create();
    BuildingSecurityGraph segraph = BuildingSecurityGraph.getInstance();
    Set<CyberLinkEdge> cyberLinks = segraph.getCyberLinks();
    Set<CyberLinkAnalysis> cyberLinkAn = new HashSet<CyberLinkAnalysis>();
    
    Map<String, List<String>> followers = new HashMap<String, List<String>>();
    for(CyberLinkEdge cl : cyberLinks)
    {
      String id1 = cl.getIdObject1();
      String id2 = cl.getIdObject2();
      String name = cl.getName();
      List<String> ids= null;
      ids = followers.get(name);
      if(ids == null)
          ids = new ArrayList<String>();
      ids.add(id1);
      ids.add(id2);
      followers.put(name, ids);
    }
    Set<CyberLinkAnalysis> cas = new HashSet<CyberLinkAnalysis>();
    for(Entry<String, List<String>> idNF : followers.entrySet())
    {
      String label = idNF.getKey();
      List<String> followerss = idNF.getValue();
      String source = mostCommon(followerss);
      List<String> targets = removeString(source, followerss);
      
      CyberLinkAnalysis ca = new CyberLinkAnalysis(label, source, targets)  ;
      cas.add(ca);
    }
    String cyberLinksAnalisis = g.toJson(cas);
    
    return cyberLinksAnalisis;
  }
  
  private List<String> removeString(String x, List<String> lst)
  {
    while(lst.contains(x))
    {
      lst.remove(x);
    }
    return lst;
  }
  
  private String mostCommon(List<String> lst)
  {
    int frequency = 0;
    int fmax =  0;
    for(String s : lst)
    {
       frequency = java.util.Collections.frequency(lst, s);
       if(frequency > fmax)
       {
         fmax = frequency;
         
       }
    }
    for(String s : lst)
    {
       frequency = java.util.Collections.frequency(lst, s);
       if(frequency == fmax)
       {
         return s;
         
       }
    }
    
    
    
    
    return "";
  }
  
  
  public String getLinksCoupleFormat() {
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
    
    public CyberLinkAnalysis(String label, String source, List<String> targets)
    {
      this.label = label;
      this.source = source;
      for(String target : targets)
      {
        this.targets.add(target);
      }
      
    }
    
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
    
    public BuildingObjSimple(Asset boc, String bRoomId)
    {
      //BASIC
      String id = boc.getId();
      AssetType type = boc.getType();
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
