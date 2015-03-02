package com.eteks.sweethome3d.swing.objstatus;

import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.JLifeStatusPanel;

public class JStatusLifePanelDec extends JPanelStatusDecorator {

  private JLifeStatusPanel jLifeStatusPanel;
  
  public JStatusLifePanelDec(JPanelStatusDecorator panel, StatusOfObjectForView status) {
    super(panel, "statusPanel" ,status);
    
  }

  @Override
  public void addSpecificComponent() {
    jLifeStatusPanel = new JLifeStatusPanel("lifePanel");
    this.jLifeStatusPanel.setLifeSatus(this.initialStatusPanel.getLifeStatus());
    this.addPanel(jLifeStatusPanel, "lifePanel", 1);
  }

  @Override
  protected   StatusOfObjectForView getOwnStatus()
  {
    return new StatusOfObjectForView(jLifeStatusPanel.getLifeSatus(), null);
  }


}
