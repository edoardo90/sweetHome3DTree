package com.eteks.sweethome3d.swing.objstatus.tables;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import com.eteks.sweethome3d.adaptive.security.assets.Asset;
import com.eteks.sweethome3d.viewcontroller.HomeController;


public  class TableContainmentPanel extends PanelWithTable<String> {

  private static final long serialVersionUID = 2562326598955980071L;
  private List<HomeController> controllers = new ArrayList<HomeController>();
  
  public  TableContainmentPanel(List<String> rows, List<HomeController> homeControllers) {
    super( new TableContainmentModel(rows, getHeaders()));
    this.controllers = homeControllers;
    this.assignDoubleClickListener();
  }

  private void assignDoubleClickListener()
  {
    this.table.addMouseListener(new MouseAdapter() {
    
      public void mousePressed(MouseEvent me) {
          JTable table =(JTable) me.getSource();
          Point tp = me.getPoint();
          int row = table.rowAtPoint(tp);
          if (me.getClickCount() == 2) {
              String rowDoubleClick  = getRowAt(row);
              System.out.println(rowDoubleClick);
              TableContainmentModel tcm = (TableContainmentModel)table.getModel();
              String id  = tcm.getId(rowDoubleClick);
              askAViewForChangingStatus(id);
              
          }
      }
  });
  }
  
  private Asset askAViewForChangingStatus(String id)
  {
     for(HomeController hc : this.controllers)
     {
       Asset boc = hc.editStatusOfOjbect(id);
       return boc;
     }
     return null;
  }
  
  
  private static List<String> getHeaders()
  {
    List<String> headers = new ArrayList<String>();
    headers.add("ID");
    headers.add("Name");
    headers.add("Type");
    return headers;
  }
  
  
  public static class TableContainmentModel extends TableListStringsModel
  {

    private static final long serialVersionUID = 7802266303976780357L;

    public TableContainmentModel(List<String> rows, List<String> header) {
      super(rows, header);
     
    }

    @Override
    protected String niceString(String s) {
        return s;
    }
    
    public String getId(String row)
    {
      return row.split(",")[0];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
         return false;
    }


    
  }
  
  
}
