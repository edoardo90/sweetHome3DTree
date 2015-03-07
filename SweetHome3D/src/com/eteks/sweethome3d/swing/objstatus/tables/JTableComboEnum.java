package com.eteks.sweethome3d.swing.objstatus.tables;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;


public class JTableComboEnum extends JTable {
  private static final long serialVersionUID = 1123121312L;
  private      List<Class<? extends Enum>> enumColumns =
      new ArrayList<Class<? extends Enum>>();
  
  
  
  public JTableComboEnum(TableModel mod) {
      super(mod);
  }


  public void setColumnEnum(Class<? extends Enum> enumClass )
  {
    this.enumColumns.add(enumClass);
  }

  

  /**
   * Requires previous and smart invocations of setColumnEnum method 
   * If no enum colums were setted, it just return the defaul Cell Editor
   */
  public TableCellEditor getCellEditor(int row, int column)
  {
    Class<? extends Enum> enumClass = null;
    try
    {
      enumClass = this.enumColumns.get(column);
    }
    catch(IndexOutOfBoundsException e)
    {
      System.err.println("enum Column not foud, probably the setColumn method was not yet called");
    }
    if(enumClass == null)
      return super.getCellEditor(row, column);
    else
    {
      
      List<String> possibleValues = null;
      try
      {
        possibleValues = this.toStringList(enumClass);
      }
      catch(Exception e )
      {
        System.err.println("it was not possible to get List<Srting> from " + enumClass);
        return super.getCellEditor(row, column);
      }
      return this.createCellEditorFromList(possibleValues);
    }
  }

  protected TableCellEditor createCellEditorFromList(List<String> possibleValues) {
    
    JComboBox<String> comboValues = this.createComboFromList(possibleValues);
    return new DefaultCellEditor(comboValues);
    
  }


  protected JComboBox<String> createComboFromList(List<String> values)
  {
    JComboBox<String> cb = new JComboBox<String>();
    for(String s: values)
    {
      cb.addItem(s);
    }
    return cb;
  }
  
  private  <T extends Enum<T>> List<String> toStringList(Class<T> clz) {
    try {
      List<String> res = new LinkedList<String>();
      Method getDisplayValue = clz.getMethod("name");

      for (Object e : clz.getEnumConstants()) {
        res.add((String) getDisplayValue.invoke(e));
      }

      return res;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }


}
