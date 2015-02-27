package com.eteks.sweethome3d.swing.objstatus;

public abstract class JPanelStatusDecorator extends JPanelColor {
  
  protected JPanelColor decoratedPanel;
  protected StatusOfObjectForView status;
  
  /**
   * Assign status variable and call drawOnPanel method
   * @param decoratedPanel
   * @param status
   */
  public JPanelStatusDecorator(JPanelColor panel, String name, StatusOfObjectForView status) 
  {
      super(name);
      this.decoratedPanel = panel;
      this.status = status;
      
  } 
  
  public abstract StatusOfObjectForView getStatus();
  
  
  
}
