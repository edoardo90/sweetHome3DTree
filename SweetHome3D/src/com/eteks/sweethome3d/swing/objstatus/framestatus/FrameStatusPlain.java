package com.eteks.sweethome3d.swing.objstatus.framestatus;

import javax.swing.JFrame;

import com.eteks.sweethome3d.swing.objstatus.JPanelStatusDecorator;
import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;

public class FrameStatusPlain extends FrameStatusAbstract {
 
  private static final long serialVersionUID = -4352134181321596489L;
  protected JPanelStatusDecorator statusDecorator;
  
  public FrameStatusPlain(JPanelStatusDecorator mainPanel, JFrame parent, String name) {
    super( parent, name);
    this.statusDecorator = mainPanel;
    this.getContentPane().add(this.statusDecorator);
    this.pack();
  }

  @Override
  public StatusOfObjectForView getRepresentation() {
    return this.statusDecorator.getComplexiveStatus();
  }

}
