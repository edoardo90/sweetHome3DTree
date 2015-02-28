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
    if(this.rows != null && !this.rows.contains(s))
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

  public String getRowAt(int row)
  {
    return this.rows.get(row);
  }
  /**
   * render
   */
  public Object getValueAt(int row, int col) {
    String fileStr = this.getRowAt(row);
    String [] colss = fileStr.split(",");
    return niceString(colss[col]);
  }

  protected abstract String niceString(String s);

  
  /*
   * JTable uses this method to determine the default renderer/
   * editor for each cell.  If we didn't implement this method,
   * then the last column would contain text ("true"/"false"),
   * rather than a check box.
   */
  public Class<? extends Object> getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }


  public  abstract boolean isCellEditable(int row, int col) ;
  
  
  public void setValueAt(Object value, int row, int col) {
    if (DEBUG) {
      System.out.println("Setting value at " + row + "," + col
          + " to " + value
          + " (an instance of "
          + value.getClass() + ")");
    }
    try
    {
      String interestedRow  = this.rows.get(row);
      int indexOfInterestedColumn = this.rows.indexOf(interestedRow);
      String[] interestedColumns = interestedRow.split(",");
      interestedColumns[col] = (String)value;
      interestedRow = rejoinColumnFromStrings(interestedColumns);
      this.rows.remove(indexOfInterestedColumn);
      this.rows.add(indexOfInterestedColumn, interestedRow);

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

  private String rejoinColumnFromStrings(String [] interestedColumns) {
    String s = "";
    for(int i=0; i<this.getColumnCount(); i++)
    {
      s = s + interestedColumns[i];
    }
    return s;
  }


  private void printDebugData() {
    for(String s : this.rows)
    {
      System.out.println(s);
    }
  }
}
