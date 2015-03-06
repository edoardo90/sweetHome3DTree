package com.eteks.sweethome3d.swing.objstatus.framestatus;

import javax.swing.JFrame;

import com.eteks.sweethome3d.swing.objstatus.representation.CyberLinkRepr;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.JPanelCyberName;

public class FrameStatusString extends FrameStatusAbstractStateLess {

  private static final long serialVersionUID = 2667434493690431514L;
  private JPanelCyberName cyberPanel;

  public FrameStatusString(JFrame parent, String name, JPanelCyberName panel) {
    super(parent, name);
    this.add(panel);
  }
  
  public CyberLinkRepr getStatus()
  {
    return this.cyberPanel.getCyberLinkRepr();
  }

  public JPanelCyberName getCyberPanel() {
    return cyberPanel;
  }

  public void setCyberPanel(JPanelCyberName cyberPanel) {
    this.cyberPanel = cyberPanel;
  }

  public void setStatus(CyberLinkRepr cyberLink) {
    this.cyberPanel = new JPanelCyberName(cyberLink, "Edit Cyber Link");
    
  }

}
