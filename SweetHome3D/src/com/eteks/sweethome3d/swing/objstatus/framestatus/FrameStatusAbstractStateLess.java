package com.eteks.sweethome3d.swing.objstatus.framestatus;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;


/**
 * Intermediate class of frame that can return a representation of state 
 * @author Edoardo Pasi
 */
public abstract class FrameStatusAbstractStateLess extends JDialog {
  
  /**
   * <pre>
   * Just call super(parent, name, true) 
   * Used to pass the parent frame to JDialog
   * </pre>

   * @param parent  the parent frame
   * @param name  the name of the window
   */
  public FrameStatusAbstractStateLess(JFrame parent, String name)
  {
    super(parent, name, true);
  }
  
}
