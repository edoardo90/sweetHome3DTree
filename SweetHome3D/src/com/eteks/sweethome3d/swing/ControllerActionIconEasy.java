package com.eteks.sweethome3d.swing;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import com.eteks.sweethome3d.junit.adaptive.actions.resources.toolbarButtonGraphics.navigation.IMG;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.resources.icons.IMGRES;

public class ControllerActionIconEasy  extends AbstractAction {
  
    private static final long serialVersionUID = -2359663477903602357L;
    private final Object    controller;
    private final Method    controllerMethod;
    private final Object [] parameters;

    public ControllerActionIconEasy(String text, String iconName,
                            String desc, Integer mnemonic,
                            Object controller, 
                            String method, 
                            Object ... parameters) throws NoSuchMethodException {
      
      super(text, createNavigationIcon(iconName));
      putValue(SHORT_DESCRIPTION, desc);
      putValue(MNEMONIC_KEY, mnemonic);
      
      this.controller = controller;
      this.parameters = parameters;

      Class<?> [] parametersClass = new Class [parameters.length];
      for(int i = 0; i < parameters.length; i++)
        parametersClass [i] = parameters [i].getClass();
      
      this.controllerMethod = controller.getClass().getMethod(method, parametersClass);
    }

    /**
     * Calls the method on controller given in constructor.
     */
     
    public void actionPerformed(ActionEvent ev) {
      try {
        this.controllerMethod.invoke(controller, parameters);
      } catch (IllegalAccessException ex) {
        throw new RuntimeException (ex);
      } catch (InvocationTargetException ex) {
        throw new RuntimeException (ex);
      }
    }
    
    
    
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createNavigationIcon(String imageName) {
        String imgLocation = ""
                             + imageName
                             + ".png";
        java.net.URL imageURL = IMGRES.class.getResource(imgLocation);

        if (imageURL == null) {
            System.err.println("Resource not found: "
                               + imgLocation);
            return null;
        } else {
            return new ImageIcon(imageURL);
        }
    }

    
    
 
  }

 
