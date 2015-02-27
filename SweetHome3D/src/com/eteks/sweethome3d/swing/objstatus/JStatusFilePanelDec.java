package com.eteks.sweethome3d.swing.objstatus;

public class JStatusFilePanelDec extends JPanelStatusDecorator {

  private FileStatusPanel fileStatusPanel;

  public JStatusFilePanelDec(JPanelColor panel, StatusOfObjectForView status) {
    super(panel, "statusFile" ,status);
    
  }


  @Override
  public StatusOfObjectForView getStatus() {
    return new StatusOfObjectForView(status.getLifeStatus(), fileStatusPanel.getFiles());
  }

}
