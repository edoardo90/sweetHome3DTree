package com.eteks.sweethome3d.swing.objstatus.tables;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class TableListModel extends AbstractTableModel {
  private static final long serialVersionUID = 1L;
  private static boolean DEBUG = true;
  
  protected List<String> rows = new ArrayList<String>();
  private List<String> colsHeader = new ArrayList<String>();

  public TableListModel(List<String> rows, List<String> header)
  {
    for(String row : rows)
    {
      this.addRow(row);
    }
    this.colsHeader.addAll(header);
  }


  public List<String> getRows() {
    return this.rows;
  }


  public int getColumnCount() {
    return colsHeader.size();
  }

  public int getRowCount() {
    return rows.size();
  }

  public String getColumnName(int col) {
    return colsHeader.get(col);
  }

  public  void addRow(String s)
  {
    this.rows.add(s);
  }
 

  public void removeRow(int row)
  {
    try
    {
      this.rows.remove(row);
    }
    catch(IndexOutOfBoundsException e)
    {

    }

  }


  /**
   * render
   */
  public Object getValueAt(int row, int col) {
    String fileStr = this.rows.get(row);
    String [] colss = fileStr.split(",");
    return niceString(colss[col]);
  }
  
  private String niceString(String s)
  {
    return "";
  }
  
  /*
   * JTable uses this method to determine the default renderer/
   * editor for each cell.  If we didn't implement this method,
   * then the last column would contain text ("true"/"false"),
   * rather than a check box.
   */
  public Class<? extends Object> getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }

  /*
   * Don't need to implement this method unless your table'niceString
   * editable.
   */
  public boolean isCellEditable(int row, int col) {
    //Note that the data/cell address is constant,
    //no matter where the cell appears onscreen.
    if (col == 0) {
      return false;
    } else {
      return true;
    }
  }

  /*
   * Don't need to implement this method unless your table'niceString
   * data can change.
   */
  public void setValueAt(Object value, int row, int col) {
    if (DEBUG) {
      System.out.println("Setting value at " + row + "," + col
          + " to " + value
          + " (an instance of "
          + value.getClass() + ")");
    }
    try
    {
      String rowT  = this.rows.get(row);
      int index = this.rows.indexOf(rowT);
      String[] colss = rowT.split(",");
      colss[col] = (String)value;
      rowT = colss[0] + "," + colss[1] + "," + colss[2];
      this.rows.remove(index);
      this.rows.add(index, rowT);

      fireTableCellUpdated(row, col);

      if (DEBUG) {
        System.out.println("New value of data:");
        printDebugData();
      }
    }
    catch(IndexOutOfBoundsException e)
    {

    }
  }

  private void printDebugData() {
    for(String s : this.rows)
    {
      System.out.println(s);
    }
  }
}
