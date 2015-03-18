package com.eteks.sweethome3d.swing.snap;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.SwingViewFactory;
import com.eteks.sweethome3d.viewcontroller.HomeController;
import com.eteks.sweethome3d.viewcontroller.HomeView;

public class ViewFactorySimple extends SwingViewFactory {

  public ViewFactorySimple() {
  }
  
  @Override
  /**
   * Returns a new view that displays <code>home</code> and its sub views.
   */
  public HomeView createHomeView(Home home, UserPreferences preferences,
                                 HomeController homeController) {
    return new HomePaneSnap(home, preferences, homeController);
  }

}
