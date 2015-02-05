package com.eteks.sweethome3d.junit.adaptive;


import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.viewcontroller.HomeController;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

public class ControllerTest extends HomeController {
  public ControllerTest(Home home, 
                        UserPreferences preferences,
                        ViewFactory viewFactory) {

    super(home, preferences, viewFactory);
    new ViewTest(this).displayView();
  }
 
}