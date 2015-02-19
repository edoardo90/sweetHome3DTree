package com.eteks.sweethome3d.swing.objstatus;

import javax.swing.JDialog;
import javax.swing.JFrame;



public abstract class FrameStatusAbstract extends JDialog {
  
  public FrameStatusAbstract(JFrame parent, String name)
  {
    super(parent, name, true);
   
  }
  
  public abstract StatusOfObjectForView getRepresentation();
  
  
}
