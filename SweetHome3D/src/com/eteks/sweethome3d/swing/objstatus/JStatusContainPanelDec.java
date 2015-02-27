package com.eteks.sweethome3d.swing.objstatus;

import java.util.ArrayList;

import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.JPanelColor;

public class JStatusContainPanelDec extends JPanelStatusDecorator {

  public JStatusContainPanelDec(JPanelColor panelToDecore,  StatusOfObjectForView status) {
    super(panelToDecore, "containementPanel", status);

  }

  @Override
  public StatusOfObjectForView getStatus() {
        return new StatusOfObjectForView("", new ArrayList<String>());
  }

  @Override
  public void addSpecificComponent() {
    // TODO Auto-generated method stub

  }

}
