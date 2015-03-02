package com.eteks.sweethome3d.swing.objstatus.tables;
import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.FileObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.NonDisclose;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.SecurityLevel;
/**
 * This class provide a panel containing a table of files
 * 
 * @author Edoardo Pasi
 */
public class TableFilePanel extends PanelWithTable<String> {

  private static final long serialVersionUID = 2300698587964822917L;
  private static boolean DEBUG = true;
  

  public TableFilePanel(List<String> files) {
     super(new TableFileModel(files));
  }

  
  
  static class TableFileModel extends TableListStringsModel {

    private static final long serialVersionUID = 1L;

    private List<String> files = new ArrayList<String>();
    
    public TableFileModel(List<String> files)
    {
        super(files, getFileHeaders());
        this.files = files;
    }
    
    private static List<String> getFileHeaders()
    {
      List<String> colsHeaderL = new ArrayList<String>();
      colsHeaderL.add("File absolute Path");
      colsHeaderL.add("Security Level");
      colsHeaderL.add("Non disclosure");
      return colsHeaderL;
    }

    public List<String> getFiles() {
      return this.files;
    }

    @Override
    public void addRow(String s)
    {
      try
      {
          FileObject fob = new FileObject(s);
          String repr = fob.getFileRepresentationForTable();
          if(files == null)
            files = new ArrayList<String>();
          if(!files.contains(repr))
          {
            this.files.add(repr);
            this.rows.add(repr);
          }
      }
      catch(Exception e ){
        e.printStackTrace();
      }

    }

    @Override
    public void removeRow(int row)
    {
      try
      {
        this.files.remove(row);
      }
      catch(IndexOutOfBoundsException e)
      {

      }

    }

    
    @Override
    protected String niceString(String s)
    {
      try{
      try{
        SecurityLevel  lev = SecurityLevel.valueOf(s);
        return lev.toString();
      }
      catch(Exception e)
      {
        NonDisclose nd = NonDisclose.valueOf(s);
        return nd.toString();
      }}
      catch(Exception e)
      {
        return s;
      }
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
      //Note that the data/cell address is constant,
      //no matter where the cell appears onscreen.
      if (col == 0) {
        return false;
      } else {
        return true;
      }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
      if (DEBUG) {
        System.out.println("Setting value at " + row + "," + col
            + " to " + value
            + " (an instance of "
            + value.getClass() + ")");
      }
      try
      {
        String rowT  = this.files.get(row);
        int index = this.files.indexOf(rowT);
        String[] colss = rowT.split(",");
        colss[col] = (String)value;
        rowT = colss[0] + "," + colss[1] + "," + colss[2];
        this.files.remove(index);
        this.files.add(index, rowT);

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
      for(String s : this.files)
      {
        System.out.println(s);
      }
    }
  }




}