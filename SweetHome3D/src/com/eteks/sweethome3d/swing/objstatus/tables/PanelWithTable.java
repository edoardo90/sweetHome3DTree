package com.eteks.sweethome3d.swing.objstatus.tables;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;


public abstract class PanelWithTable<T> extends JPanel {

  private static final long serialVersionUID = 5231836378143782925L;
  protected JTable table;

  public PanelWithTable(TableListModel<T> tableModel)
  {
    super(new GridLayout(1,0));

    TableListModel<T> mod = tableModel;
    table = new JTable(mod);
    table.setPreferredScrollableViewportSize(new Dimension(500, 70));
    table.setFillsViewportHeight(true);

    this.setCancKeyboardRemove();
    JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane);
  }

  public  void addRow(T row)
  {
    try
    {
      TableListModel<T> tableModel = (TableListModel<T>) this.table.getModel();
      tableModel.addRow(row);
      this.table.repaint();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }
  public void addAllRows(List<T> lst)
  {
    for(T row: lst)
    {
      this.addRow(row);
    }
  }

  public T getRowAt(int row) {
    try{
      return ((TableListModel<T>) this.table.getModel()).getRowAt(row);
    }
    catch(Exception e)
    {
      return null;
    }
  }
  
  public List<T> getRows() {
    try{
      return ((TableListModel<T>) this.table.getModel()).getRows(); 
    }
    catch(Exception e)
    {
      return null;
    }
  }
  
  public List<T> getSelectedRows() {
    try{
      
      int [] selectedIndexes = this.table.getSelectedRows();
      List<T> selectedIndexesStr = new ArrayList<T>();
      for(int i=0; i<selectedIndexes.length; i++)
      {
        T selectedRowStr = this.getRowAt(selectedIndexes[i]);
        selectedIndexesStr.add(selectedRowStr);
      }
      return selectedIndexesStr;
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
      private static final long serialVersionUID = -2998813927043726560L;

      public void actionPerformed(ActionEvent e) {
        TableModel mod = table.getModel();

        if (mod instanceof TableListModel) {
          TableListModel mtm = (TableListModel)mod;

          int rowToDelete = table.getSelectedRow();
          System.out.println("remove row : " + rowToDelete);
          mtm.removeRow(rowToDelete);
          table.repaint();
        }
      }
    });

  }


}
