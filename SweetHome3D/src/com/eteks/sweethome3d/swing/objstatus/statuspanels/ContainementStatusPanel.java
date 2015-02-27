package com.eteks.sweethome3d.swing.objstatus.statuspanels;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class ContainementStatusPanel extends JPanelColor {

  private static final long serialVersionUID = -3380325228386416664L;

  public ContainementStatusPanel(String name) {
    super(name);
    BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
    this.setLayout(box);
    
    JPanel radioP = new JPanel();
    radioP.setAlignmentX(LEFT_ALIGNMENT);
    this.add(radioP);
    
    radioP.setBackground(Color.CYAN);
    this.addComponents();
    
    Border bspace = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    this.setBorder(BorderFactory.createTitledBorder(bspace, "asd"));
  }
  
  private void addComponents()
  {
    
    JButton b = new JButton("ksdjfklsdj sdfasdf ");
    this.add(b);
    
    JButton c = new JButton("ksdjfklsdj sdfasdf ");
    this.add(c);
    
    
    System.out.println("asdjas ");
    
    
  }
  
  public void setStatus(String lifeStatus) {
    // TODO update the panel, or the status of the panel or whatever
    
  }

  
  
}
