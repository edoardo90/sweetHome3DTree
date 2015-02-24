package com.eteks.sweethome3d.swing.objstatus;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.policy.Role;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.ActorObject;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.HomeFurniturePanel;
import com.eteks.sweethome3d.swing.SwingTools;
import com.eteks.sweethome3d.viewcontroller.HomeFurnitureController;

public class HomeFurnitureActor extends HomeFurniturePanel {

  private final ActorObject actor ;
  
  public HomeFurnitureActor(ActorObject actor, UserPreferences preferences, HomeFurnitureController controller) {
      super(preferences, controller);
      this.actor = actor;
      this.addRolePanel(2);
  }
  
  private void shiftDownPanels(int firstRowAffected, int lastRowAffected, int downOf)
  {
    for(JPanel p : super.getPanels())
    {
      int nowRow = super.getRowOfPanel(p);
      if(nowRow <= lastRowAffected && nowRow >= firstRowAffected)
      {
        nowRow += downOf;
        super.changeRowToPanel(p, nowRow);
      }
        
    }
  }
  
  
  private void addRolePanel(int row)
  {
    
    this.shiftDownPanels(2, this.getPanels().size(), 1);

    //Role panel
    int gridx=0, gridy=1, gridwidth=1, gridheight=1;
    int weightx=0, weighty=0;
    int anchor=GridBagConstraints.CENTER, fill=GridBagConstraints.NONE,  ipadx=0, ipady=0;
    Insets insets = new Insets(0, 0, 0, 0);
    
    JPanel rolePanel = SwingTools.createTitledPanel("Role");
    
    anchor=GridBagConstraints.LINE_START;
    gridwidth=2; weightx=0;
    rolePanel.add(new JLabel("Add role:"), new GridBagConstraints(gridx, gridy, gridwidth, gridheight,
        weightx, weighty, anchor, fill, new Insets(0, 10, 0, 0), ipadx, ipady));
    
    gridx=2;
    gridwidth=1;
    weightx=1;
    
    anchor=GridBagConstraints.LINE_START;
    rolePanel.add(this.getRolesComponent(), new GridBagConstraints
        (gridx, gridy, gridwidth, gridheight, 
         weightx, weighty,
         anchor, fill, new Insets(10, 10, 10, 10), ipadx, ipady));
    ipadx=0;
    
    gridx=0; gridy=row; gridwidth=3; weightx=1; fill = GridBagConstraints.BOTH;
    GridBagConstraints c = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, 
        weightx, weighty, anchor, fill, insets, ipadx, ipady);
    
    this.constraintsPanel.put(rolePanel.getName(), c);
    
    this.add(rolePanel, c);

  }
  
  private Component getRolesComponent() {
    
     List<Role> actorRoles = this.actor.getRoles();
     JList<String> swRoles = new JList(actorRoles.toArray());
     
    
    return swRoles;
  }

  private void shiftDown(Component cc, int firstRow)
  {
    
    for(Component c :  getComponentsCast(cc))
    {
      if (c instanceof JPanel) {
        JPanel panel = (JPanel)c;
        
        if (panel.getComponents() != null && panel.getComponents().length != 0)
        {
          
          GridBagLayout glay = new GridBagLayout();
          GridBagConstraints constrGot =  glay.getConstraints(c);
          String panelName = panel.getName();
          
          System.out.println("panel:" + panelName 
                 + "constr map: " + this.niceContrainsts(panelName,  this.constraintsPanel.get(panelName)));
          System.out.println("panel " + panelName + " says : i contain");
          shiftDown(c, 0);
        }
     }
      else
      {
        System.out.println("\t *other*");
      }
    }
 }
 

  
  private Component [] getComponentsCast(Component cc) {
    if (cc instanceof JComponent) {
      JComponent ccj = (JComponent)cc;
      return ccj.getComponents();
    }
    return null;
  }
  
  
  private String niceContrainsts(String name, GridBagConstraints c)
  {
    String s = "\n";
    if(c== null)
      return "name is :     " + name +  " and gridContrs is null...";
    s = s + "gridx:         " + c.gridx + "\n" +
            "gridy:         " + c.gridy + "\n" +
            "gridHeight:    " + c.gridheight + "\n" +
            "gridWidth:     " + c.gridwidth +  "\n" +
            "ipadx:         " + c.ipadx;
    return s;
  }
  
  
}
