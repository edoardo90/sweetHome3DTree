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
      case SHOW_STATUS:
      {
         text = "Edit Status";
         iconName = "edit_status";
         desc = "Click to edit the status of the object";
         mnemonic = new Integer(KeyEvent.VK_E);
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
      // TODO Auto-generated catch block
      ex.printStackTrace();
      return null;
    }

    
  }
  
}
