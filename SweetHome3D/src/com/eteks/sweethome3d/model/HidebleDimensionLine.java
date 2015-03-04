package com.eteks.sweethome3d.model;

public class HidebleDimensionLine extends DimensionLine {

  private static final long serialVersionUID = 1975021907804458535L;
  private boolean visible = true;
  private String name="pippomio!";
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
 
}
