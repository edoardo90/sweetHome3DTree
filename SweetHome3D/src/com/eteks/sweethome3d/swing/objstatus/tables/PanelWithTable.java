package com.eteks.sweethome3d.swing.objstatus.tables;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.eteks.sweethome3d.swing.objstatus.tables.TableFilePanel.TableFileModel;


public abstract class PanelWithTable extends JPanel {

  private static final long serialVersionUID = 5231836378143782925L;
  protected JTable table;

  public PanelWithTable(AbstractTableModel tableModel)
  {
    super(new GridLayout(1,0));

    AbstractTableModel mod = tableModel;
    table = new JTable(mod);
    table.setPreferredScrollableViewportSize(new Dimension(500, 70));
    table.setFillsViewportHeight(true);

    this.setCancKeyboardRemove();
    JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane);
  }

  public  void addRow(String s)
  {
    try
    {
      TableListModel tableModel = (TableListModel) this.table.getModel();
      tableModel.addRow(s);
      this.table.repaint();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }
  public void addAllRows(List<String> lst)
  {
    for(String s: lst)
    {
      this.addRow(s);
    }
  }

  public String getRowAt(int row) {
    try{
      return ((TableListModel) this.table.getModel()).getRowAt(row);
    }
    catch(Exception e)
    {
      return null;
    }
  }
  
  public List<String> getRows() {
    try{
      return ((TableListModel) this.table.getModel()).getRows(); 
    }
    catch(Exception e)
    {
      return null;
    }
  }
  
  public List<String> getSelectedRows() {
    try{
      
      int [] selectedIndexes = this.table.getSelectedRows();
      List<String> selectedIndexesStr = new ArrayList<String>();
      for(int i=0; i<selectedIndexes.length; i++)
      {
        String selectedRowStr = this.getRowAt(selectedIndexes[i]);
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
