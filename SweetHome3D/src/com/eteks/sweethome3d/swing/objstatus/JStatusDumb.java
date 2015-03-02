package com.eteks.sweethome3d.swing.objstatus;

import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;

public class JStatusDumb extends JPanelStatusDecorator {

  public JStatusDumb(String name) {
    super(name);
  }

  @Override
  public void addSpecificComponent() {
    return;
  }

  @Override
  protected   StatusOfObjectForView getOwnStatus()
  {
    return null;
  }
  


}
