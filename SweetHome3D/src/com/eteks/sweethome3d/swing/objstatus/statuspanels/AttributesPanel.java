package com.eteks.sweethome3d.swing.objstatus.statuspanels;

import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.attributes.BuildingObjectAttribute;
import com.eteks.sweethome3d.swing.objstatus.tables.PanelWithTable;
import com.eteks.sweethome3d.swing.objstatus.tables.TableAttributesPanel;
import com.eteks.sweethome3d.swing.objstatus.tables.TableListModel;

public class AttributesPanel extends PanelWithTable<BuildingObjectAttribute> {

  private static final long serialVersionUID = 5301699016077956912L;
  
  private JButton addAttributeBtn;
  private TableAttributesPanel tableAttributes;
  private final boolean editable ;
  
  public AttributesPanel(TableListModel<BuildingObjectAttribute> tableModel, boolean editable) {
    super(tableModel);
    
    this.editable = editable;
    if(this.editable)
    {    this.addAttributeBtn = new JButton("Add Attribute");
         this.add(addAttributeBtn);
    }
    
  }
  
  public void setStatus(List<BuildingObjectAttribute> attrs )
  {
    this.tableAttributes = new TableAttributesPanel(attrs, this.editable);
  }
  
  public List<BuildingObjectAttribute> getAttributes()
  {
    return this.tableAttributes.getRows();
  }
  

}
