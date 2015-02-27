package com.eteks.sweethome3d.swing;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.ActorObject;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.viewcontroller.HomeFurnitureController;
/**
 * This class provides a panel with checkboxes to select roles associated to
 * agents
 * @author Edoardo Pasi
 */
public class HomeFurnitureActor extends HomeFurniturePanel implements ItemListener{

  private final Set<String> roles;
  private Set<String> selectedRoles = new HashSet<String>();
  
  public HomeFurnitureActor(Set<String> roles, ActorObject actor, UserPreferences preferences, HomeFurnitureController controller) {
      super(preferences, controller);
      this.roles = roles;
      this.selectedRoles.addAll(actor.getRolesStr());
      this.addRolePanel(2);
  }
  
  public Set<String> getSelectedRoles()
  {
    return this.selectedRoles;
  }

  public void itemStateChanged(ItemEvent e) {
    int state = e.getStateChange();  // 1 = ON  2 = OFF
    System.out.println(state);
    String text = ((JCheckBox) e.getItem() ).getText();
    if(state==1)
    {
      this.selectedRoles.add(text);
    }
    else if(state == 2)
    {
      this.selectedRoles.remove(text);
    }
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

    //Role decoratedPanel
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
    
    this.constraintsPanelMap.put(rolePanel.getName(), c);
    
    this.add(rolePanel, c);

  }
  
  private Component getRolesComponent() {
    
    JPanel panelRolesCheck = new JPanel();
    panelRolesCheck.setLayout(new GridBagLayout());
    Insets insets = new Insets(10, 10, 10, 10);
    int row=0;
    for(String role : this.roles)
    {
      panelRolesCheck.add(new JLabel(role), new GridBagConstraints
          (/*gridx */0,
           /*gridy*/row, 
           /*gridwidth*/1,
           /*gridheight*/ 1,
           /* weightx */ 0, 
           /* weighty */0,
           /*anchor */ GridBagConstraints.CENTER, 
           /*fill*/ GridBagConstraints.BOTH,
           insets, 
           /*ipadx*/ 0,
           /*ipady*/0));
      JCheckBox c1 = new JCheckBox(role);
      if(this.selectedRoles.contains(role))
             c1.setSelected(true);
      c1.addItemListener(this);
      panelRolesCheck.add(c1, new GridBagConstraints
          (/*gridx */1,
              /*gridy*/row, 
              /*gridwidth*/1,
              /*gridheight*/ 1,
              /* weightx */ 0, 
              /* weighty */0,
              /*anchor */ GridBagConstraints.CENTER, 
              /*fill*/ GridBagConstraints.BOTH,
              insets, 
              /*ipadx*/ 0,
              /*ipady*/0));
      row++;
    }
    return panelRolesCheck;
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
          
          System.out.println("decoratedPanel:" + panelName 
                 + "constr map: " + this.niceContrainsts(panelName,  this.constraintsPanelMap.get(panelName)));
          System.out.println("decoratedPanel " + panelName + " says : i contain");
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
