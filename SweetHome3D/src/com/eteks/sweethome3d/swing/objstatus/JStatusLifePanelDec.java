package com.eteks.sweethome3d.swing.objstatus;

import java.util.ArrayList;

import javax.swing.JComponent;

import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.JLifeStatusPanel;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.JPanelColor;

public class JStatusLifePanelDec extends JPanelStatusDecorator {

  private JLifeStatusPanel jLifeStatusPanel;
  
  public JStatusLifePanelDec(JPanelColor panel, StatusOfObjectForView status) {
    super(panel, "statusPanel" ,status);
    
  }


  @Override
  public StatusOfObjectForView getStatus() {
    return new StatusOfObjectForView("ON", new ArrayList<String>());    
  }


  @Override
  public void addSpecificComponent() {
    jLifeStatusPanel = new JLifeStatusPanel("lifePanel");
    this.jLifeStatusPanel.setLifeSatus(this.initialStatusPanel.getLifeStatus());
    this.addPanel(jLifeStatusPanel, "lifePanel");
    
  }


  
  

}
