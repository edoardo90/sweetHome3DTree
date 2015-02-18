package com.eteks.sweethome3d.swing.objstatus;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class FrameStatus extends JDialog {
  

  private static final long serialVersionUID = -6273908450419161637L;
  private List<String> files = new ArrayList<String>();
  private String lifeStatus = "";
  private LifeStatusPanel lifeStatusPanel;
  private JPanel contentPane = new JPanel();
  
  public FrameStatus(StatusOfObjectForView statusObject, JFrame  parent, String name)
  {
    super(parent, name, true);
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    lifeStatusPanel = new LifeStatusPanel();
    lifeStatusPanel.setLifeSatus(statusObject.lifeStatus);
    lifeStatusPanel.setOpaque(true);
    
    contentPane = makeContainerNice(contentPane);
    this.setContentPane(contentPane);
    
    
    contentPane.add(lifeStatusPanel);
    
    
    this.setLocationRelativeTo(null);
    this.pack();
  }
  
  private JPanel makeContainerNice(JPanel frame)
  {
    frame.setLayout(new GridBagLayout());
    JPanel panel = new JPanel();
    
    panel.setBorder(new LineBorder(Color.BLACK)); // make it easy to see
    
    frame.add(panel, new GridBagConstraints());
    frame.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
    
    frame.setBackground(Color.CYAN);
    
    return frame;
  }
  
  
  
  public StatusOfObjectForView getRepresentation()
  {
    lifeStatus = lifeStatusPanel.getLifeSatus();
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
