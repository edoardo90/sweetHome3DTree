package com.eteks.sweethome3d.swing.objstatus.tables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

public class JTablePredefinedValues extends JTableComboEnum {

  private Map<Integer, List<String>> predefinedValuesCol = new HashMap<Integer, List<String>>();
  
  public JTablePredefinedValues(TableModel mod) {
    super(mod);
 
  }
  
  public void setColumnPredefinedValues(Integer column, List<String> predefinedeValues)
  {
    this.predefinedValuesCol.put(column, predefinedeValues);
  }

  @Override
  public TableCellEditor getCellEditor(int row, int column)
  {
     TableCellEditor ed = super.getCellEditor(row, column);
     if(ed == null)
     {
       List<String> possibleValues = predefinedValuesCol.get(new Integer(column));
       if(possibleValues != null && possibleValues.size() > 1)
       {
           TableCellEditor cellEdit =  super.createCellEditorFromList(possibleValues);
           System.out.println("cell editor : " + cellEdit);
       }
     }
     
     return ed;
  }
  
}
