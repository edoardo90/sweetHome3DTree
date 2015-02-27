package com.eteks.sweethome3d.swing.objstatus;

import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.JPanelColor;

public abstract class JPanelStatusDecorator extends JPanelColor {
  
  protected StatusOfObjectForView initialStatusPanel;
  
  /**
   * Assign initialStatusPanel variable and call drawOnPanel method
   * @param decoratedPanel
   * @param initialStatusPanel
   */
  public JPanelStatusDecorator(JPanelColor panelToDecore, String name, StatusOfObjectForView status) 
  {
      super(name);
      this.initialStatusPanel = status;
      for(JPanelColor coloredP : panelToDecore.getPanels())
      {
        this.addPanel(coloredP, coloredP.getName());
      }
      this.addSpecificComponent();
  } 
  
  public abstract StatusOfObjectForView getStatus();
  public abstract void addSpecificComponent();
}
