package com.eteks.sweethome3d.swing.opendialog;

import java.awt.Window;

import javax.swing.SwingUtilities;

import com.eteks.sweethome3d.SweetHome3D;
import com.eteks.sweethome3d.resources.Res;
import com.eteks.sweethome3d.swing.SwingTools;

public class DemoSplash {

  private Window w;
  public void openSplash()
  {
     w = SwingTools.showSplashScreenWindowW(Res.class.getResource("splashScreen.jpg"));
    
  }
  
  public void closeSplash()
  {
    if(w != null)
      w.dispose();
  }
  
  public static void main(String [] aweaw)
  {
    DemoSplash ds = new DemoSplash();
    ds.openSplash();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
    ds.closeSplash();
  }
  
}
