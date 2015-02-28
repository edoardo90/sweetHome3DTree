package com.eteks.sweethome3d.swing.objstatus.tables;

import java.util.ArrayList;
import java.util.List;


public  class TableContainmentPanel extends PanelWithTable {

  private static final long serialVersionUID = 2562326598955980071L;

  public  TableContainmentPanel(List<String> rows) {
    super( new TableContainmentModel(rows, getHeaders()));
  }

  private static List<String> getHeaders()
  {
    List<String> headers = new ArrayList<String>();
    headers.add("ID");
    headers.add("Type");
    return headers;
  }
  
  
  public static class TableContainmentModel extends TableListModel
  {

    private static final long serialVersionUID = 7802266303976780357L;

    public TableContainmentModel(List<String> rows, List<String> header) {
      super(rows, header);
     
    }

    @Override
    protected String niceString(String s) {
        return s;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
         return false;
    }
    
  }
  
  
}
