package com.eteks.sweethome3d.junit.adaptive;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;

import com.eteks.sweethome3d.viewcontroller.HomeController;

public  class ViewTest extends JRootPane {
  public ViewTest(final HomeController controller) {
    // Display main view in this pane
    getContentPane().add((JComponent)controller.getView());
  }

  public void displayView() {
    JFrame frame = new JFrame("Home Controller Test") {
      {
        setRootPane(ViewTest.this);
      }
    };
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);


  } 
}