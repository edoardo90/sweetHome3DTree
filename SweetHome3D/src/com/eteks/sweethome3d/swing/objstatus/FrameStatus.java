package com.eteks.sweethome3d.swing.objstatus;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class FrameStatus extends FrameStatusAbstract {
  

  private static final long serialVersionUID = -6273908450419161637L;
  private List<String> files = new ArrayList<String>();
  private String lifeStatus = "";
  private LifeStatusPanel lifeStatusPanel;
  private FileStatusPanel fileStatusPanel;
  private Container container;
  private JPanel    containerPanel = new JPanel();

  
  public FrameStatus(StatusOfObjectForView statusObject, JFrame  parent, String name)
  {
    super(parent, name);
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    
    lifeStatusPanel = new LifeStatusPanel();
    lifeStatusPanel.setLifeSatus(statusObject.getLifeStatus());
    
    
    fileStatusPanel = new FileStatusPanel();
    fileStatusPanel.setFileStatus(statusObject.getFiles());
    
    container = this.getContentPane();
    
    container.add(containerPanel);
    
    
    containerPanel.setLayout(new BorderLayout());
    
    containerPanel.add(lifeStatusPanel, BorderLayout.NORTH);
    containerPanel.add(fileStatusPanel, BorderLayout.CENTER);
    
    this.setContentPane(container);
    this.setLocationRelativeTo(null);
    this.pack();
  }
  

  
  public StatusOfObjectForView getRepresentation()
  {
    lifeStatus = lifeStatusPanel.getLifeSatus();
    files = fileStatusPanel.getFiles();
    return new StatusOfObjectForView(lifeStatus, files);
  }
  
  }
