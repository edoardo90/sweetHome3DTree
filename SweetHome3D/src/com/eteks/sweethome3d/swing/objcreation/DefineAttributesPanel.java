package com.eteks.sweethome3d.swing.objcreation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.attributes.BuildingObjectAttribute;
import com.eteks.sweethome3d.swing.objstatus.tables.PanelWithTable;

public class DefineAttributesPanel extends PanelWithTable<BuildingObjectAttribute> {

  public DefineAttributesPanel(AbstractTableModel tableModel) {
    super(tableModel);
  }
  private static List<String> getHeaders()
  {
    List<String> headers = new ArrayList<String>();
    headers.add("Type");
    headers.add("Name");
    headers.add("Default");
    return headers;
  }

}
