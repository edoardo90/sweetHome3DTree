package com.eteks.sweethome3d.swing.objstatus.tables;

import java.util.List;

public abstract class TableListStringsModel extends TableListModel<String>{

  private static final long serialVersionUID = 8133020082207861101L;
  private boolean DEBUG = true;

  public TableListStringsModel(List<String> rows, List<String> header) {
    super(rows, header);
  }

  public Object getValueAt(int row, int col)
  {
    String fileStr = this.getRowAt(row);
    String [] colss = fileStr.split(",");
    return niceString(colss[col]);

  }

  @Override
  protected abstract String niceString(String s);


  @Override
  public abstract boolean isCellEditable(int row, int col);

  @Override
  public void setValueAt(Object value, int row, int col)
  {
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
    for(String row : this.rows)
    {
      System.out.println(row);
    }
  }



}
