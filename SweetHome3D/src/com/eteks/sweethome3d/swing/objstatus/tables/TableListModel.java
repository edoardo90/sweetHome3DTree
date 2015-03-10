package com.eteks.sweethome3d.swing.objstatus.tables;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class TableListModel<T> extends AbstractTableModel {
  private static final long serialVersionUID = 1L;
  private static boolean DEBUG = true;
  
  protected List<T> rows = new ArrayList<T>();
  private List<String> colsHeader = new ArrayList<String>();
  public TableListModel(List<T> rows, List<String> header)
  {
    for(T row : rows)
    {
      this.addRow(row);
    }
    this.colsHeader.addAll(header);
  }

  

  public List<T> getRows() {
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

  public  void addRow(T row)
  {
    if(this.rows != null && !this.rows.contains(row))
        this.rows.add(row);
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

  public T getRowAt(int row)
  {
    return this.rows.get(row);
  }
  /**
   * render
   */
  public abstract Object getValueAt(int row, int col);


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
  
  public abstract void setValueAt(Object value, int row, int col) ;


}
