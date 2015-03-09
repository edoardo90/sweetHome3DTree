package com.eteks.sweethome3d.model;

import com.eteks.sweethome3d.swing.objstatus.representation.CyberLinkRepr;

public class HidebleDimensionLine extends DimensionLine {

  private static final long serialVersionUID = 1975021907804458535L;
  private boolean visible = true;
  private String name="";
  
  private String id1 = "";
  private String id2 = "";
  
  float xEndOriginal=0, yEndOriginal=0, xStartOriginal = 0, yStartOriginal = 0;
  final float BIG = 1000;
  
  public HidebleDimensionLine(float xStart, float yStart, float xEnd, float yEnd, float offset) {
    super(xStart, yStart, xEnd, yEnd, offset);
    
    this.xEndOriginal = xEnd;
    this.yEndOriginal = yEnd;
    
    this.xStartOriginal = xStart;
    this.yStartOriginal = yStart;
  }
  
  public void setVisibility(boolean visible)
  {
    this.visible = visible;
    if(this.visible == false)
    {
      this.setXEnd(BIG);
      this.setXStart(BIG);
      
      this.setYEnd(BIG);
      this.setYStart(BIG);
    }
    else
    {
      this.setXEnd(this.xEndOriginal);
      this.setXStart(this.xStartOriginal);
      
      this.setYStart(this.yStartOriginal);
      this.setYEnd(this.yEndOriginal);
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void updateStatus(CyberLinkRepr cyberTransformed) {
    this.name = cyberTransformed.getCyberName();
    this.setXEnd(this.getXEnd()+0.0001f);
    
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
 
}
