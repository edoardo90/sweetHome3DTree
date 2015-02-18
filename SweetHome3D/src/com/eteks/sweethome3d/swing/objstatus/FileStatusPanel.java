package com.eteks.sweethome3d.swing.objstatus;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class FileStatusPanel extends JPanel {

  private List<String> files = new ArrayList<String>();
  private JLabel generalTitleLab = new JLabel("These are the files stored in the device");
  
  private JPanel tablePanel;
  
  public FileStatusPanel()
  {
    BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
    
    generalTitleLab.setAlignmentY(TOP_ALIGNMENT);
    generalTitleLab.setAlignmentX(CENTER_ALIGNMENT);
    this.setLayout(box);
    this.add(generalTitleLab);
    this.add(Box.createRigidArea(new Dimension(0, 20)));
    
    JPanel buttonAndTable = this.borderedPanelCenterVertical(20, Color.GREEN);
    
    
    JPanel buttonPanel = this.borderedPanelCenterHorizontal(20, Color.magenta);
    
    JButton addFileBtn = new JButton("Add File");
    JLabel  addFileDescription = new JLabel("Click to Add a File");
    
    buttonPanel.add(addFileDescription);
    buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    buttonPanel.add(addFileBtn);
    
    tablePanel = this.borderedPanelCenterVertical(20, Color.ORANGE);
    
    
    
    buttonAndTable.add(buttonPanel);
    buttonAndTable.add(tablePanel);
    
    
    this.add(buttonAndTable);
    this.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
  }

  public void setFileStatus(List<String> files) {
   this.files = files;
   
   List<String> filesMut = new ArrayList<String>();
   filesMut.addAll(this.getFiles());
   
   this.tablePanel.add(new TableFilePanel(filesMut));
  }
  
  public List<String> getFiles()
  {
     return this.files;
  }
  

  private JPanel borderedPanelCenterHorizontal(int bord, Color color)
  {
    JPanel jp = new JPanel();
    jp.setBorder(BorderFactory.createEmptyBorder(bord, bord, bord, bord));
    jp.setAlignmentX(CENTER_ALIGNMENT);
    jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
    jp.setBackground(color);
    return jp;
  }
  
  
  private JPanel borderedPanelCenterVertical(int bord, Color color)
  {
    JPanel jp = new JPanel();
    jp.setBorder(BorderFactory.createEmptyBorder(bord, bord, bord, bord));
    jp.setAlignmentX(CENTER_ALIGNMENT);
    jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
    jp.setBackground(color);
    return jp;
  }
  

}
