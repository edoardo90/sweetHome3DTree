package com.eteks.sweethome3d.swing.objstatus;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class FrameStatusJustLife extends FrameStatusAbstract {
  

  private static final long serialVersionUID = -6273908450419161637L;
  private List<String> files = new ArrayList<String>();
  private String lifeStatus = "";
  private JLifeStatusPanel jLifeStatusPanel;
  private Container container;
  private JPanel    containerPanel = new JPanel();

  
  public FrameStatusJustLife(StatusOfObjectForView statusObject, JFrame  parent, String name)
  {
    super(statusObject, parent, name);
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    
    jLifeStatusPanel = new JLifeStatusPanel();
    jLifeStatusPanel.setLifeSatus(statusObject.getLifeStatus());
    
    container = this.getContentPane();
    
    container.add(containerPanel);
    
    
    containerPanel.setLayout(new BorderLayout());
    
    containerPanel.add(jLifeStatusPanel, BorderLayout.NORTH);
    
    this.setContentPane(container);
    this.setLocationRelativeTo(null);
    this.pack();
  }
  
  @Override
  public StatusOfObjectForView getRepresentation()
  {
    lifeStatus = jLifeStatusPanel.getLifeSatus();
    files = null;
    return new StatusOfObjectForView(lifeStatus, files);
  }

  
  }
