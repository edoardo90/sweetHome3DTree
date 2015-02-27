package com.eteks.sweethome3d.swing.objstatus;

import javax.swing.JComponent;

public class JStatusLifePanelDec extends JPanelStatusDecorator {

  private JLifeStatusPanel jLifeStatusPanel;
  
  public JStatusLifePanelDec(JPanelColor panel, StatusOfObjectForView status) {
    super(panel, "statusPanel" ,status);
   
  }



  @Override
  public StatusOfObjectForView getStatus() {
    
    String lifeStatus = jLifeStatusPanel.getLifeSatus();
    return new StatusOfObjectForView(lifeStatus, this.status.getFiles());
  }


  
  

}
