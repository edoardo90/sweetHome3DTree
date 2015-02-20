package com.eteks.sweethome3d.swing;

import java.awt.event.KeyEvent;

import javax.swing.Action;

import com.eteks.sweethome3d.viewcontroller.HomeView.ActionType;

public class ActionCoolFactory {

  public Action createAction(ActionType type, Object controller, String methodName)
  {
    
    String text = "", iconName = "", desc = ""; Integer mnemonic = 0;
    
    switch(type)
    {
      case OPEN_IFC:
      {
        text = "Open Ifc File";
        iconName = "open_ifc";
        desc = "Click to import a home from an Ifc File";
        mnemonic = new Integer(KeyEvent.VK_I);
        break;
      }
      
      case SHOW_STATUS:
      {
         text = "Edit Status";
         iconName = "edit_status";
         desc = "Click to edit the status of the object";
         mnemonic = new Integer(KeyEvent.VK_E);
         break;
      }
      case ADD_FILE:
      {
        text = "Add new File";
        iconName = "add_file";
        desc = "Click to add a new file";
        mnemonic = new Integer(KeyEvent.VK_A); 
        break;
      }
      
      default:
      {
       
        text = "Click";
        iconName = "edit_status";
        desc = "Just click";
        mnemonic = new Integer(KeyEvent.VK_E);
        
        break;
      }
      
    }
    
    
    try {
      Action a = new ControllerActionIconEasy(text, iconName, desc, mnemonic, controller, methodName);
      return a;
    } catch (Exception ex) {
      
      ex.printStackTrace();
      return null;
    }

    
  }
  
}
