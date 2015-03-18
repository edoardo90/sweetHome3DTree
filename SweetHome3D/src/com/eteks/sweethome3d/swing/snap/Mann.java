package com.eteks.sweethome3d.swing.snap;

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

public class Mann {

  public Mann() {
    // TODO Auto-generated constructor stub
  }

  public  static void main(String [] args) {
    ViewFactory viewFactory = new  ViewFactorySimple();
    UserPreferences preferences = new DefaultUserPreferences();
    preferences.setUnit(LengthUnit.METER);
    Home home = new Home();
    ControllerSimpler t = new ControllerSimpler(home, preferences, viewFactory);
    
    Container f = t.displayView();
    
    JFrame f2 = new JFrame();
    Container containerPane = f2.getContentPane();
    
    JPanel p = new JPanel();
    p.setLayout(new FlowLayout());
    
    p.add(new JButton("woowo"));
    p.add(f);
    p.add(new JButton("woowo"));
    
    containerPane.add(p);
    
    f2.pack();
    f2.setDefaultCloseOperation(3);
    f2.setVisible(true);
    
  }
  
}
