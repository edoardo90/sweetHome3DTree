package com.eteks.sweethome3d.swing.objstatus.framestatus;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.FileStatusPanel;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.JLifeStatusPanel;


public class FrameStatus extends FrameStatusAbstract {
  

  private static final long serialVersionUID = -6273908450419161637L;
  private List<String> files = new ArrayList<String>();
  private String lifeStatus = "";
  private JLifeStatusPanel jLifeStatusPanel;
  private FileStatusPanel fileStatusPanel;
  private Container container;
  private JPanel    containerPanel = new JPanel();

  
  public FrameStatus(StatusOfObjectForView statusObject, JFrame  parent, String name)
  {
    super(statusObject, parent, name);
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    
    jLifeStatusPanel = new JLifeStatusPanel("life status");
    jLifeStatusPanel.setLifeSatus(statusObject.getLifeStatus());
    
    
    fileStatusPanel = new FileStatusPanel("file__StatusPanel");
    fileStatusPanel.setFileStatus(statusObject.getFiles());
    
    container = this.getContentPane();
    
    container.add(containerPanel);
    
    
    containerPanel.setLayout(new BorderLayout());
    
    containerPanel.add(jLifeStatusPanel, BorderLayout.NORTH);
    containerPanel.add(fileStatusPanel, BorderLayout.CENTER);
    
    this.setContentPane(container);
    this.setLocationRelativeTo(null);
    this.pack();
  }
  

  @Override
  public StatusOfObjectForView getRepresentation()
  {
    lifeStatus = jLifeStatusPanel.getLifeSatus();
    files = fileStatusPanel.getFiles();
    return new StatusOfObjectForView(lifeStatus, files);
  }
  
  }
