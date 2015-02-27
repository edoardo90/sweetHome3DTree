package com.eteks.sweethome3d.swing.objstatus;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 



/*
 * TableFilePanel.java requires no other files.
 */

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultCellEditor;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.FileObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.NonDisclose;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.SecurityLevel;

/** 
 * TableFilePanel is just like SimpleTableDemo, except that it
 * uses a custom TableModel.
 */
public class TableFilePanel extends JPanel {
  private boolean DEBUG = true;
  private JTable table;

  public TableFilePanel(List<String> files) {
    super(new GridLayout(1,0));

    AbstractTableModel mod = new TableFileModel(files);
    table = new JTable(mod);


    table.setPreferredScrollableViewportSize(new Dimension(500, 70));
    table.setFillsViewportHeight(true);

    this.setColumnCombos();
    this.setCancKeyboardRemove();

    //Create the scroll pane and add the table to it.
    JScrollPane scrollPane = new JScrollPane(table);

    //Add the scroll pane to this decoratedPanel.
    add(scrollPane);
  }

  public void addRow(String s)
  {
    try{
      ((TableFileModel) this.table.getModel()).addRow(s);
      this.table.repaint();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  } 

  public List<String> getFiles() {

    try{
      return ((TableFileModel) this.table.getModel()).getFiles();

    }
    catch(Exception e)
    {
      return null;
    }
  }


  private void setCancKeyboardRemove() {
    int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
    InputMap inputMap = table.getInputMap(condition);
    ActionMap actionMap = table.getActionMap();

    // DELETE is a String constant that for me was defined as "Delete"
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "Delete");
    actionMap.put("Delete", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        TableModel mod = table.getModel();
        if (mod instanceof TableFileModel) {
          TableFileModel mtm = (TableFileModel)mod;

          int rowToDelete = table.getSelectedRow();
          System.out.println("remove row : " + rowToDelete);
          mtm.removeRow(rowToDelete);
          table.repaint();
        }
      }
    });

  }

  private void setColumnCombos()
  {
    JComboBox<String> securityLevelCombo = new JComboBox<String>();
    for(SecurityLevel sl : SecurityLevel.values())
    {
      securityLevelCombo.addItem(sl.toString());
    }
    TableColumn securityLevelColumn = table.getColumnModel().getColumn(1);
    securityLevelColumn.setCellEditor(new DefaultCellEditor(securityLevelCombo));

    JComboBox<String> ndaLevelCombo = new JComboBox<String>();
    for(NonDisclose nd : NonDisclose.values())
    {
      ndaLevelCombo.addItem(nd.toString());
    }

    TableColumn ndaLevelColumn = table.getColumnModel().getColumn(2);
    ndaLevelColumn.setCellEditor(new DefaultCellEditor(ndaLevelCombo));

  }

  class TableFileModel extends AbstractTableModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<String> files = new ArrayList<String>();
    private List<String> colsHeader = new ArrayList<String>();

    public TableFileModel(List<String> files)
    {
      
      for(String fileString : files)
      {
        this.addRow(fileString);
      }
      

      colsHeader.add("File absolute Path");
      colsHeader.add("Security Level");
      colsHeader.add("Non disclosure");
    }


    public List<String> getFiles() {
      return this.files;
    }


    public int getColumnCount() {
      return colsHeader.size();
    }

    public int getRowCount() {
      return files.size();
    }

    public String getColumnName(int col) {
      return colsHeader.get(col);
    }

    public void addRow(String s)
    {
      try
      {
          FileObject fob = new FileObject(s);
          String repr = fob.getFileRepresentationForTable();
          if(!files.contains(repr))
              this.files.add(repr);
      }
      catch(Exception e ){
        e.printStackTrace();
      }

    }

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


    /**
     * render
     */
    public Object getValueAt(int row, int col) {
      String fileStr = this.files.get(row);
      String [] colss = fileStr.split(",");
      return niceString(colss[col]);
    }
    
    private String niceString(String s)
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
    
    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c) {
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