package com.eteks.sweethome3d.swing.objstatus.statuspanels;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.attributes.BuildingObjectAttribute;
import com.eteks.sweethome3d.swing.objstatus.tables.TableAttributesPanel;

public class AttributesStatusPanel extends JPanelColorStatefull<BuildingObjectAttribute> {

  private static final long serialVersionUID = 5301699016077956912L;
  
  private JButton addAttributeBtn;
  private TableAttributesPanel tableAttributes;
  private final boolean nameAndTypeEditable ;
  
  public AttributesStatusPanel( boolean nameAndTypeEditable) {
    super("Attribute Panel");
    
    BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
    this.setLayout(box);
    
    this.nameAndTypeEditable = nameAndTypeEditable;
    if(  this.nameAndTypeEditable ) //if so the table is designed to define attributes 
    {   
        
        this.addAttributeBtn = new JButton("Add Attribute");
        this.add(addAttributeBtn);
    }
    
    Border bspace = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    this.setBorder(BorderFactory.createTitledBorder(bspace, "Attributes"));
    
  }
 
  @Override
  public void setStatus(List<BuildingObjectAttribute> attrs )
  {
    this.tableAttributes = new TableAttributesPanel(attrs, this.nameAndTypeEditable);
    this.add(this.tableAttributes);
  }
  
  public List<BuildingObjectAttribute> getAttributes()
  {
    return this.tableAttributes.getRows();
  }
  

}
