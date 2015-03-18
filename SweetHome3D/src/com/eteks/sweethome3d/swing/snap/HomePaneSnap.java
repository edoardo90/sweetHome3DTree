package com.eteks.sweethome3d.swing.snap;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.HomePane;
import com.eteks.sweethome3d.viewcontroller.HomeController;

public class HomePaneSnap extends HomePane {

  public HomePaneSnap(Home home, UserPreferences preferences, HomeController controller) {
    super(home, preferences, controller);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  protected JComponent createMainPane(Home home, UserPreferences preferences, 
                                      HomeController controller) {
      final JComponent catalogFurniturePane = super.createCatalogFurniturePane(home, preferences, controller);
      final JComponent planView3DPane = super.createPlanView3DPane(home, preferences, controller);

      if (catalogFurniturePane == null) {
        return planView3DPane;
      } else if (planView3DPane == null) {
        return catalogFurniturePane;
      } else {
        final JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,   //one at the side of other (left n right)
            catalogFurniturePane, planView3DPane);

        
        mainPane.setDividerLocation(360); 

        return planView3DPane;

      }
    }

  

}
