/*
 * HomeControllerTest.java 15 mai 2006
 * 
 * Copyright (c) 2006 Emmanuel PUYBARET / eTeks <info@eteks.com>. All Rights
 * Reserved.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.eteks.sweethome3d.junit.adaptive;

import java.util.List;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;

import junit.framework.TestCase;

import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.model.CatalogPieceOfFurniture;
import com.eteks.sweethome3d.model.FurnitureCategory;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.PieceOfFurniture;
import com.eteks.sweethome3d.model.RoomGeoSmart;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.FurnitureCatalogTree;
import com.eteks.sweethome3d.swing.FurnitureTable;
import com.eteks.sweethome3d.swing.SwingViewFactory;
import com.eteks.sweethome3d.viewcontroller.FurnitureCatalogController;
import com.eteks.sweethome3d.viewcontroller.FurnitureController;
import com.eteks.sweethome3d.viewcontroller.HomeController;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

/**
 * Tests home controller.
 * @author Emmanuel Puybaret
 */
public class FurnitureAddTest extends BasicTest {
  private ViewFactory          viewFactory;
  private UserPreferences      preferences;
  public  Home                 home;
  private HomeController       homeController;
  private FurnitureCatalogTree catalogTree;
  private FurnitureController  furnitureController;
  private FurnitureTable       furnitureTable;

  @Override
  protected void setUp() {
    this.viewFactory = new SwingViewFactory();
    this.preferences = new DefaultUserPreferences();
    this.preferences.setFurnitureCatalogViewedInTree(true);
    this.home = new Home();
    this.homeController = 
        new HomeController(this.home, this.preferences, viewFactory);
    FurnitureCatalogController catalogController = 
        homeController.getFurnitureCatalogController();
    this.catalogTree = (FurnitureCatalogTree)catalogController.getView();
    this.furnitureController = 
        homeController.getFurnitureController();
    this.furnitureTable = 
        (FurnitureTable)furnitureController.getView();

  }

  
  public static class ControllerTest extends HomeController {
    public ControllerTest(Home home, 
                          UserPreferences preferences,
                          ViewFactory viewFactory) {

      super(home, preferences, viewFactory);
      new ViewTest(this, home, preferences).displayView();

   }

  }
  
  public  static void main(String [] args) {
    ViewFactory viewFactory = new SwingViewFactory();
    UserPreferences preferences = new DefaultUserPreferences();
    preferences.setUnit(LengthUnit.METER);
    Home home = new Home();
    ControllerTest t = new ControllerTest(home, preferences, viewFactory);
    
    FurnitureAddTest f = new FurnitureAddTest();
    f.doStuffInsideMain(home, preferences);
    
  }

  
  public HomePieceOfFurniture getDoor()
  {
    HomePieceOfFurniture door = null;
    Locale.setDefault(Locale.US);
    // Read default user preferences
    UserPreferences preferences = new DefaultUserPreferences();
    List<FurnitureCategory> categories= preferences.getFurnitureCatalog().getCategories();
    for(FurnitureCategory category : categories )
    {
      List<CatalogPieceOfFurniture> catalogObjs = category.getFurniture();
      for(PieceOfFurniture piece : catalogObjs)
      {
                  String pieceName = piece.getName();
        if(pieceName.equals("Door"))
        {
          door = new HomePieceOfFurniture(piece);
        }
      }
    }    
    return door;
  }

  

  private static class ViewTest extends JRootPane {
    private Home home;
    private UserPreferences userPreferences;
    
    public ViewTest(final HomeController controller, final Home home, final UserPreferences pref) {
      
      this.home = home;
      this.userPreferences = pref;
      
      getContentPane().add((JComponent)controller.getView());
    }

    public void displayView() {
      JFrame frame = new JFrame("Change language") {
        {
          setRootPane(ViewTest.this);
        }
      };
      frame.pack();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);

    } 
    
  }

  @Override
  public  void doStuffInsideMain(Home home, UserPreferences preferences){
      HomePieceOfFurniture door = this.getDoor();
      home.addPieceOfFurniture(door);
  }
}
