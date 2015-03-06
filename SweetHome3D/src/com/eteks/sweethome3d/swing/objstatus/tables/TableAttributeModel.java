package com.eteks.sweethome3d.swing.objstatus.tables;

import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.attributes.AttributeType;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.attributes.BuildingObjectAttribute;

public class TableAttributeModel extends TableListModel<BuildingObjectAttribute>
{
  private static final long serialVersionUID = -5589968849280582764L;
  private final boolean nameAndTypeEditable;
  
  public TableAttributeModel(List<BuildingObjectAttribute> rows, 
                                   List<String> header, boolean nameAndTypeEditable) {
    super(rows, header);
    this.nameAndTypeEditable = nameAndTypeEditable;
  }

  @Override
  public Object getValueAt(int row, int col) {
    
    BuildingObjectAttribute attr = this.getRowAt(row);
    if(col == 0)
      return attr.getName();
    else if (col == 1)
      return attr.getType();
    else if (col == 2)
      return attr.getValue();
    
    return null;
  }

  @Override
  protected String niceString(String s) {
    return null;
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    if(col == 2)   //default value  or setted value, changhable in any case
      return true;
    if(col == 0 || col == 1)   //name and type are changeble if the aim of table is creating
      return this.nameAndTypeEditable;
    return false;
  }

  @Override
  public void setValueAt(Object value, int row, int col) {
     BuildingObjectAttribute attr = this.getRowAt(row);
     
     if(col == 0)
     {
       attr.setNameTypeValue( new BuildingObjectAttribute((String)value, attr.getType(), attr.getValue()));
     }
     else if (col == 1)
     {
       attr.setNameTypeValue( new BuildingObjectAttribute(attr.getName(), (AttributeType)value, attr.getValue()));
     }
     else if (col == 2)
     {
       attr.setNameTypeValue( new BuildingObjectAttribute(attr.getName(), attr.getType(), value));
     }
     
  }
  
}

