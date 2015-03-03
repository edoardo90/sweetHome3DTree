package com.eteks.sweethome3d.swing.objstatus;

import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.FileStatusPanel;

public class JStatusFilePanelDec extends JPanelStatusDecorator {

  private FileStatusPanel fileStatusPanel;

  public JStatusFilePanelDec(JPanelStatusDecorator panel, StatusOfObjectForView status) {
    super(panel, "statusFile" ,status);
    
  }


  @Override
  public void addSpecificComponent() {
    fileStatusPanel = new FileStatusPanel("Status Panel");
    fileStatusPanel.setFileStatus(this.initialStatusObjectForView.getFiles());
    this.addPanel(fileStatusPanel, "file_-_-StatusPanel", 5);
  }

  @Override
  protected   StatusOfObjectForView getOwnStatus()
  {
    return new StatusOfObjectForView(null, this.fileStatusPanel.getFiles());
  }


  
  
}
