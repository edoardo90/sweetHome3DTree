package com.eteks.sweethome3d.swing.objstatus.tables;

import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.attributes.BuildingObjectAttribute;

public class TableAttributesPanel extends PanelWithTable<BuildingObjectAttribute> {

  private static final long serialVersionUID = 7302485009260274212L;

  public TableAttributesPanel(List<BuildingObjectAttribute> rows, boolean editable) {
    super(new TableDefineAttributeModel(rows, getHeaders(), editable ));
  }
  
  private static List<String> getHeaders()
  {
    List<String> headers = new ArrayList<String>();
    
    headers.add("Name");
    headers.add("Type");
    headers.add("Default");
    return headers;
  }
  
  
}