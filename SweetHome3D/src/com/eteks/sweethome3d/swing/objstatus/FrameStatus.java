package com.eteks.sweethome3d.swing.objstatus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class FrameStatus extends JDialog {
  
  private List<String> files = new ArrayList<String>();
  private String lifeStatus = "";
  private LifeStatusPanel lifeStatusPanel;
  private JPanel contentPane = new JPanel();
  
  public FrameStatus(JFrame  parent, String name)
  {
    super(parent, name, true);
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    lifeStatusPanel = new LifeStatusPanel();
    lifeStatusPanel.setOpaque(true);
    
    contentPane = makeContainerNice(contentPane);
    this.setContentPane(contentPane);
    
    contentPane.add(lifeStatusPanel);
    
    //this.setSize(600, 400);
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
  
  
  
  public StatusOfObjectView getRepresentation()
  {
    lifeStatus = lifeStatusPanel.getLifeSatus();
    return new StatusOfObjectView(lifeStatus, files);
  }

  
  public  class StatusOfObjectView
  {
    private final String lifeStatus;
    private final List<String> files;
    
    public StatusOfObjectView(final String lifeStatus, final List<String> files)
    {
      this.lifeStatus =  lifeStatus;
      this.files = Collections.unmodifiableList(files);
    }

    public String getLifeStatus() {
      return lifeStatus;
    }

    public List<String> getFiles() {
      return files;
    }
  }
}
