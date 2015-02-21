package com.eteks.sweethome3d.adaptive.security.buildingGraph;

public class CyberLinkEdge {
  
  private String idObject1;
  private String idObject2;
  public CyberLinkEdge(String id1, String id2) {
    this.idObject1 = id1;
    this.idObject2 = id2;
  }
  public String getIdObject1() {
    return idObject1;
  }
  public void setIdObject1(String idObject1) {
    this.idObject1 = idObject1;
  }
  public String getIdObject2() {
    return idObject2;
  }
  public void setIdObject2(String idObject2) {
    this.idObject2 = idObject2;
  }

  @Override
  public String toString()
  {
    return this.idObject1 + "<::::::::::::>" + this.idObject2 + "\n";
  }

  
  @Override 
  public boolean equals(Object o)
  {
    
    if (o instanceof CyberLinkEdge) {
      CyberLinkEdge oc = (CyberLinkEdge)o;
      
      String id1 = this.idObject1;
      String id2 = this.idObject2;
      
      String other1 = oc.idObject1;
      String other2 = oc.idObject2;
      
      boolean oneAndOne = id1.equals(other1);
      boolean oneAndTwo = id1.equals(other2);
      boolean twoAndOne = id2.equals(other1);
      boolean twoAndtwo = id2.equals(other2);
      
      return (oneAndOne  && twoAndtwo) ||  (oneAndTwo && twoAndOne);
      
             
    }
    
    return false;
  }
  
  
}
