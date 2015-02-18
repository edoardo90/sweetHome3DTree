package com.eteks.sweethome3d.swing.objstatus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class FrameStatus extends JDialog {
  

  private static final long serialVersionUID = -6273908450419161637L;
  private List<String> files = new ArrayList<String>();
  private String lifeStatus = "";
  private LifeStatusPanel lifeStatusPanel;
  private FileStatusPanel fileStatusPanel;
  private Container container;
  private JPanel    containerPanel = new JPanel();

  
  public FrameStatus(StatusOfObjectForView statusObject, JFrame  parent, String name)
  {
    super(parent, name, true);
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    
    lifeStatusPanel = new LifeStatusPanel();
    lifeStatusPanel.setLifeSatus(statusObject.getLifeStatus());
    
    
    fileStatusPanel = new FileStatusPanel();
    fileStatusPanel.setFileStatus(statusObject.getFiles());
    
    container = this.getContentPane();
    
    container.add(containerPanel);
    
    //containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
    containerPanel.setLayout(new BorderLayout());
    
    containerPanel.add(lifeStatusPanel, BorderLayout.NORTH);
    containerPanel.add(fileStatusPanel, BorderLayout.CENTER);
    
    
    container.setBackground(Color.black);
    containerPanel.setBackground(Color.yellow);
    lifeStatusPanel.setBackground(Color.CYAN);
    fileStatusPanel.setBackground(Color.orange);
    
    
    
    
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

  
  public static class StatusOfObjectForView
  {
    private final String lifeStatus;
    private final List<String> files;
    
    public StatusOfObjectForView(final String lifeStatus, final List<String> files)
    {
      this.lifeStatus =  lifeStatus;
      if(files == null)
      {
        this.files = null;
      }
      else
      {
        this.files = Collections.unmodifiableList(files);
      }
    }

    public String getLifeStatus() {
      return lifeStatus;
    }

    public List<String> getFiles() {
      return files;
    }
    
    @Override
    public String toString()
    {
      String s= "";
      s = s + " Status of Life: " + lifeStatus;
      if(files != null && files.size() != 0)
      {
        s = "\n Files Contained: \n";
        for(String fileStr : this.files)
        {
          s = s + fileStr + "\n";
        }
      }
      else
      {
        s = s + "\n No files contained";
      }
      return s;
      
    }
    
    
  }
}
