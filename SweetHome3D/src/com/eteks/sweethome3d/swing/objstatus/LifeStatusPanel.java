package com.eteks.sweethome3d.swing.objstatus;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.LifeStatus;

public class LifeStatusPanel extends JPanel implements ActionListener {

  private String lifeSatus = "ON";
  private JLabel pickStatusLab = new JLabel("Select the life status of the object");

  public LifeStatusPanel() 
  {
    BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
    pickStatusLab.setAlignmentX(CENTER_ALIGNMENT);
    this.setLayout(box);
    this.add(pickStatusLab);
    this.add(this.radioPanel()); 
    
    this.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
    
  }
  
  
  private JPanel radioPanel()
  {
    JPanel radioP = new JPanel(new BorderLayout());
    
    //Create the radio buttons.
    JRadioButton brokenRadiob = new JRadioButton(LifeStatus.BROKEN.toString());
    brokenRadiob.setMnemonic(KeyEvent.VK_B);
    brokenRadiob.setActionCommand("" + LifeStatus.BROKEN.name());
    brokenRadiob.setSelected(false);
    
    JRadioButton onRadiob = new JRadioButton(LifeStatus.ON.toString());
    onRadiob.setMnemonic(KeyEvent.VK_O);
    onRadiob.setActionCommand("" + LifeStatus.ON.name());
    onRadiob.setSelected(true);
    
    JRadioButton offRadiob = new JRadioButton(LifeStatus.OFF.toString());
    offRadiob.setMnemonic(KeyEvent.VK_S);
    offRadiob.setActionCommand("" + LifeStatus.OFF.name());
    offRadiob.setSelected(false);
    
    ButtonGroup group = new ButtonGroup();
    group.add(offRadiob);
    group.add(onRadiob);
    group.add(brokenRadiob);
    
    JPanel radioPanel = new JPanel(new GridLayout(0, 1));
    
    radioPanel.add(offRadiob);
    radioPanel.add(onRadiob);
    radioPanel.add(brokenRadiob);
    
    onRadiob.addActionListener(this);
    offRadiob.addActionListener(this);
    brokenRadiob.addActionListener(this);
    
    radioP.add(radioPanel, BorderLayout.LINE_START);
    radioP.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
    
    return radioP;
   
  }
  
  public String getLifeSatus() {
    return lifeSatus;
  }

  public void setLifeSatus(String lifeSatus) {
    this.lifeSatus = lifeSatus;
  }

  public void actionPerformed(ActionEvent e) {
      this.lifeSatus = e.getActionCommand();
      System.out.println("life status:" + this.lifeSatus);
  }
  
  
  
  
  
}
