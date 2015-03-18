package com.eteks.sweethome3d.swing.snap;

import java.awt.Container;

import javax.swing.JFrame;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.viewcontroller.HomeController;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

public class ControllerSimpler extends HomeController {
  public ControllerSimpler(Home home, 
                        UserPreferences preferences,
                        ViewFactory viewFactory) {

    super(home, preferences, viewFactory);
     
    
  }
 
  public Container displayView()
  {
    ViewPlane vp = new ViewPlane(this);
    return vp.getContentPane(); 
  }
  
}