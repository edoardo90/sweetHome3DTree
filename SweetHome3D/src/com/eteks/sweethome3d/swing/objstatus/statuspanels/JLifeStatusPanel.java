package com.eteks.sweethome3d.swing.objstatus.statuspanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.LifeStatus;

public class JLifeStatusPanel extends JPanelColor implements ActionListener {

  private String lifeSatus = "ON";
  private String title = "Select the life initialStatusObjectForView of the object";
  private JRadioButton brokenRadiob;
  private JRadioButton onRadiob;
  private JRadioButton offRadiob;

  public JLifeStatusPanel(String name) 
  {
    super(name);
    BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
    this.setLayout(box);
    
    JPanel radioP = this.radioPanel();
    radioP.setAlignmentX(LEFT_ALIGNMENT);
    this.add(radioP);
    
    radioP.setBackground(Color.CYAN);
    
    Border bspace = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    this.setBorder(BorderFactory.createTitledBorder(bspace, title));
    
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
    radioP.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    
    
    return radioP;
   
  }
  
  public String getLifeSatus() {
    return lifeSatus;
  }

  public void setLifeSatus(String lifeSatus) {
    this.lifeSatus = lifeSatus;
    if(lifeSatus == null || lifeSatus == "")
      return ;
    LifeStatus stat = LifeStatus.valueOfSmarter(lifeSatus);
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
  }
  
  
  
  
  
}
