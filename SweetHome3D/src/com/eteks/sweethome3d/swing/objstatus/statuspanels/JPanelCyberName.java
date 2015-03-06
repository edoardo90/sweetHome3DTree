package com.eteks.sweethome3d.swing.objstatus.statuspanels;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.eteks.sweethome3d.swing.objstatus.representation.CyberLinkRepr;

public class JPanelCyberName extends JPanelColor {
  private static final long serialVersionUID = -8900024549035452414L;
  private CyberLinkRepr cyberLink;
  
  private JTextField cyberNameText; 
  private JLabel     cyberNameLabel = new JLabel("CyberLink Name:");
  
  private String cyberName = "";
  
  public JPanelCyberName(CyberLinkRepr cyberLinkRepr, String name) {
    super("Edit Cyber Link");
    this.cyberLink = cyberLinkRepr;
    
    this.addComponents();
    
  }

  private void addComponents()
  {
    this.add(cyberNameLabel);
    
    this.cyberNameText = new JTextField();
    
    
    this.cyberNameText.setText(this.cyberLink.getCyberName());
    this.add(cyberNameText);
    this.cyberNameText.getDocument().addDocumentListener(new DocumentListener() {
      
      public void removeUpdate(DocumentEvent e) {
        changedUpdate(e);
        
      }
      
      public void insertUpdate(DocumentEvent e) {
        changedUpdate(e);
        
      }
      
      public void changedUpdate(DocumentEvent e) {
          cyberName = cyberNameText.getText();
          String cyb = cyberName;
          cyberLink.setCyberName(cyb);
      }
    });
    
  }
  
  public CyberLinkRepr getCyberLinkRepr() {
    
    
    System.out.println("return : " + cyberLink);
    return cyberLink;
  }

  
  
}
