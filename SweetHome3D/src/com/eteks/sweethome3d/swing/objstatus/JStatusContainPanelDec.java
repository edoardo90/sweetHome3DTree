package com.eteks.sweethome3d.swing.objstatus;

import java.util.ArrayList;

import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.ContainementStatusPanel;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.JPanelColor;

public class JStatusContainPanelDec extends JPanelStatusDecorator {

  private ContainementStatusPanel containmentPanel;
  
  public JStatusContainPanelDec(JPanelStatusDecorator panelToDecore,  StatusOfObjectForView status) {
    
       super(panelToDecore, "containementPanel", status);

  }

  
  @Override
  protected   StatusOfObjectForView getOwnStatus()
  {
    return null;
  }
  

  @Override
  public void addSpecificComponent() {
    this.containmentPanel = new ContainementStatusPanel("containmnent panel");
    this.containmentPanel.setStatus(this.initialStatusPanel.getLifeStatus());
    
    this.addPanel(this.containmentPanel, "containment panel", 1);
      
  }

}
