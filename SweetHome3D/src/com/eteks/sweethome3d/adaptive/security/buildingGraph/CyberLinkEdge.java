package com.eteks.sweethome3d.adaptive.security.buildingGraph;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.SortedSet;
import java.util.TreeSet;

public class CyberLinkEdge implements Serializable {

  
  private SortedSet<String> ids = new TreeSet<String>();
  
  private String name = "";


  
  public CyberLinkEdge(String id1, String id2) {
    this.setIdObject1(id1);
    this.setIdObject2(id2);
  }

  public String getIdObject1() {
    return this.ids.first();
  }
  public String getIdObject2() {
    return this.ids.last();
  }
  
  public void setIdObject1(String idObject1) {
    this.ids.add(idObject1);
  }

  public void setIdObject2(String idObject2) {
    this.ids.add(idObject2);
  }
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  

  @Override
  public String toString()
  {
    return this.getIdObject1() + "<::::::::::::>" + this.getIdObject2() + "\n";
  }

  
  
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.ids == null)
        ? 0
        : this.ids.hashCode());
    return result;
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CyberLinkEdge other = (CyberLinkEdge)obj;
    if (this.ids == null) {
      if (other.ids != null)
        return false;
    } else if (!this.ids.equals(other.ids))
      return false;
    return true;
  }
  public void replaceId(String oldId, String newId) {
      this.ids.remove(oldId);
      this.ids.add(newId);
    
  }

  public CyberLinkEdge createInstance(Type arg0) {
    return new CyberLinkEdge("", "");
  }

  
  
  
}
