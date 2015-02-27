package com.eteks.sweethome3d.swing.objstatus.tables;

import java.util.List;


public  class TableContainmentPanel extends PanelWithTable {

  private static final long serialVersionUID = 2562326598955980071L;

  public  TableContainmentPanel(List<String> rows, List<String> headers) {
    super( new TableContainmentModel(rows, headers));
  }

  public void addRow(String s)
  {
    try{
      ((TableContainmentModel) this.table.getModel()).addRow(s);
      this.table.repaint();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  } 
  
  public static class TableContainmentModel extends TableListModel
  {

    public TableContainmentModel(List<String> rows, List<String> header) {
      super(rows, header);
     
    }
    
  }
  
  
}
