package com.eteks.sweethome3d.swing.objstatus;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;



public abstract class FrameStatusAbstract extends JDialog {
  
  protected StatusOfObjectForView status;
  
  public FrameStatusAbstract(StatusOfObjectForView status, JFrame parent, String name)
  {
    super(parent, name, true);
    
    this.status = status;
    this.addContPlane();
  }
  
  private void addContPlane()
  {
    JPanel p = new JPanel();
    p.add(new JButton("Click to see contained files. "
        + "\n And find out just how deep the rabbit hole goes"));
    p.setBackground(Color.red);
    this.getContentPane().add(p, BorderLayout.SOUTH);
    System.out.println("skdjf");
  }
  
  
  
  public abstract StatusOfObjectForView getRepresentation();
  
  
}
