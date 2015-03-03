package com.eteks.sweethome3d.swing.objstatus;

import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.ContainementStatusPanel;
import com.eteks.sweethome3d.viewcontroller.HomeController;

public class JStatusContainPanelDec extends JPanelStatusDecorator {

  private static final long serialVersionUID = -4389972637869214433L;
  private ContainementStatusPanel containmentPanel;
  private ContPanelAim aim;
  
  public enum ContPanelAim
  {
    PICK_OBJECTS_FROM_ALL, SHOW_JUST_CONTAINED_OBJECTS
  }
  
  
  public JStatusContainPanelDec(JPanelStatusDecorator panelToDecore,  StatusOfObjectForView status, ContPanelAim aim) {
    this(panelToDecore, status, aim, null);
  }

  
  public JStatusContainPanelDec(JPanelStatusDecorator panelToDecore, StatusOfObjectForView status,
                                ContPanelAim aim, HomeController homeController) {
    super(panelToDecore, "containementPanel", status);
    this.aim = aim;
    if(aim == ContPanelAim.SHOW_JUST_CONTAINED_OBJECTS)
    {
      containmentPanel.addButtonAddObject();
      containmentPanel.addController(homeController);
    }
  }


  @Override
  protected   StatusOfObjectForView getOwnStatus()
  {
    List<String> containedObjecs = new ArrayList<String>();
    if(this.aim == ContPanelAim.PICK_OBJECTS_FROM_ALL)
    {
      containedObjecs = this.containmentPanel.getSelectedContObjects();
    }
    else if (this.aim == ContPanelAim.SHOW_JUST_CONTAINED_OBJECTS)
    {
      containedObjecs = this.containmentPanel.getAllRows();
    }
    return new StatusOfObjectForView("", containedObjecs, null, null, null);
  }
  

  @Override
  public void addSpecificComponent() {
    this.containmentPanel = new ContainementStatusPanel("containmnent panel");
    this.containmentPanel.setStatus(this.initialStatusObjectForView.getObjectContainedLst());
    
    this.addPanel(this.containmentPanel, "containment panel", 1);
      
  }

}
