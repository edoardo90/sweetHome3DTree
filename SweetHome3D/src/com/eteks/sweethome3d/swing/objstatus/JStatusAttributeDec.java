package com.eteks.sweethome3d.swing.objstatus;

import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.attributes.BuildingObjectAttribute;
import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.AttributesStatusPanel;

public class JStatusAttributeDec extends JPanelStatusDecorator {

  private static final long serialVersionUID = 421434414650160666L;
  private AttributesStatusPanel attributePanel;
  private AttributePanelAim aim;
  public enum AttributePanelAim
  {
    ADD_ATTRIBUTES, SHOW_PRESENT_ATTRIBUTES
  }
  
  
  public JStatusAttributeDec(JPanelStatusDecorator panelToDecore,  StatusOfObjectForView status, AttributePanelAim aim) {
    super(panelToDecore, "Attribute Panel", status);
    this.aim = aim;
    
  }

  @Override
  protected StatusOfObjectForView getOwnStatus() {
    
    List<BuildingObjectAttribute> attrs = this.attributePanel.getAttributes();
    return new StatusOfObjectForView(null, null, attrs, null, null);
  }

  @Override
  public void addSpecificComponent() {
    boolean editable=false;
    if(this.aim == AttributePanelAim.ADD_ATTRIBUTES)
      editable = true;
    else if (this.aim == AttributePanelAim.SHOW_PRESENT_ATTRIBUTES)
      editable = false;
    attributePanel = new AttributesStatusPanel(editable);
    attributePanel.setStatus(this.initialStatusObjectForView.getObjectAttributes());
    this.addPanel(attributePanel, "Attribute Panel");
  }

 

}