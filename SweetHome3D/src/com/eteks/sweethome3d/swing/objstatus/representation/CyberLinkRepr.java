package com.eteks.sweethome3d.swing.objstatus.representation;

public class CyberLinkRepr {

  private String  cyberName ;
  public CyberLinkRepr(String cyberName) {
    this.setCyberName(cyberName);
  }
  public String getCyberName() {
    return cyberName;
  }
  public void setCyberName(String cyberName) {
    this.cyberName = cyberName;
  }
  
  public String toString()
  {
    return "CYBERLINK:" + cyberName;
  }

}
