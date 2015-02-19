package com.eteks.sweethome3d.swing.objstatus;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.LifeStatus;

public class LifeStatusPanel extends JPanel implements ActionListener {

  private String lifeSatus = "ON";
  private JLabel pickStatusLab = new JLabel("Select the life status of the object");
  private JRadioButton brokenRadiob;
  private JRadioButton onRadiob;
  private JRadioButton offRadiob;

  public LifeStatusPanel() 
  {
    BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
    pickStatusLab.setAlignmentX(LEFT_ALIGNMENT);
    this.setLayout(box);
    this.add(pickStatusLab);
    this.add(Box.createRigidArea(new Dimension(0, 20)));
    JPanel radioP = this.radioPanel();
    radioP.setAlignmentX(LEFT_ALIGNMENT);
    this.add(radioP);
    
    
    this.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
    
  }
  
  
  private JPanel radioPanel()
  {
    JPanel radioP = new JPanel(new BorderLayout());
    
    brokenRadiob = new JRadioButton(LifeStatus.BROKEN.toString());
    brokenRadiob.setMnemonic(KeyEvent.VK_B);
    brokenRadiob.setActionCommand("" + LifeStatus.BROKEN.name());
    brokenRadiob.setSelected(false);
    
    onRadiob = new JRadioButton(LifeStatus.ON.toString());
    onRadiob.setMnemonic(KeyEvent.VK_O);
    onRadiob.setActionCommand("" + LifeStatus.ON.name());
    onRadiob.setSelected(true);
    
    offRadiob = new JRadioButton(LifeStatus.OFF.toString());
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
    LifeStatus stat = LifeStatus.valueOf(lifeSatus);
    switch(stat)
    {
      case BROKEN:
        this.brokenRadiob.setSelected(true);
        break;
      case ON:
        this.onRadiob.setSelected(true);
        break;
      case OFF:
        this.offRadiob.setSelected(true);
        break;
      default:
        
    }
    
  }

  public void actionPerformed(ActionEvent e) {
      this.lifeSatus = e.getActionCommand();
      System.out.println("life status:" + this.lifeSatus);
  }
  
  
  
  
  
}
