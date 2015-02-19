package com.eteks.sweethome3d.swing.objstatus;

import java.awt.BorderLayout;
import java.awt.Color;
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
  private LifeStatusPanel lifeStatusPanel;
  private Container container;
  private JPanel    containerPanel = new JPanel();

  
  public FrameStatusJustLife(StatusOfObjectForView statusObject, JFrame  parent, String name)
  {
    super(parent, name);
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    
    lifeStatusPanel = new LifeStatusPanel();
    lifeStatusPanel.setLifeSatus(statusObject.getLifeStatus());
    
    container = this.getContentPane();
    
    container.add(containerPanel);
    
    
    containerPanel.setLayout(new BorderLayout());
    
    containerPanel.add(lifeStatusPanel, BorderLayout.NORTH);
    
    
    container.setBackground(Color.black);
    containerPanel.setBackground(Color.yellow);
    lifeStatusPanel.setBackground(Color.CYAN);
    
    
    
    this.setContentPane(container);
    this.setLocationRelativeTo(null);
    this.pack();
  }
  

  public StatusOfObjectForView getRepresentation()
  {
    lifeStatus = lifeStatusPanel.getLifeSatus();
    files = null;
    return new StatusOfObjectForView(lifeStatus, files);
  }

  
  }
