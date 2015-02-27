package com.eteks.sweethome3d.model;

public class HidebleDimensionLine extends DimensionLine {

  private boolean visible = true;
  float xEndOriginal=0, yEndOriginal=0;
  
  
  public HidebleDimensionLine(float xStart, float yStart, float xEnd, float yEnd, float offset) {
    super(xStart, yStart, xEnd, yEnd, offset);
    this.xEndOriginal = xEnd;
    this.yEndOriginal = yEnd;
  }
  
  public void toggleVisibility()
  {
    this.visible = ! this.visible;
    
    if( ! this.visible)
    {
      super.setXEnd(this.getXEnd() + 300000);
      super.setYEnd(this.getYEnd() + 300000);
      super.setXStart(this.getXStart() + 300000);
      super.setYStart(this.getYStart() + 300000);

    }
    else
    {
      
      super.setXEnd(this.getXEnd() - 300000);
      super.setYEnd(this.getYEnd() - 300000);
      super.setXStart(this.getXStart() - 300000);
      super.setYStart(this.getYStart() - 300000);
    }
    
  }

}
