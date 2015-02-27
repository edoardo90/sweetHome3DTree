package com.eteks.sweethome3d.swing.objstatus;

import java.util.ArrayList;

import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.JPanelColor;

public class JStatusContainPanelDec extends JPanelStatusDecorator {

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
    // TODO Auto-generated method stub

  }

}
