package com.eteks.sweethome3d.swing.snap;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;

import com.eteks.sweethome3d.viewcontroller.HomeController;

public  class ViewPlane extends JRootPane {
  public ViewPlane(final ControllerSimpler controller) {

    Container cont = getContentPane(); 
    cont.add((JComponent)controller.getView());
  }

  
  public CaroselHomesFrame displayView() {
    CaroselHomesFrame frame = new CaroselHomesFrame("Home Controller Test") {
      {
        setRootPane(ViewPlane.this);
      }
    };
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    
    return frame;

  } 
}
