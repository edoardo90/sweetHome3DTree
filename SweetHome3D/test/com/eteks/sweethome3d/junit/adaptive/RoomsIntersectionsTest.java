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

import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.RoomGeoSmart;
import com.eteks.sweethome3d.model.RoomGeoSmart.intersectionAlgorithm;
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
public class RoomsIntersectionsTest extends BasicTest {
  public ViewFactory          viewFactory;
  public UserPreferences      preferences;
  public  Home                 home;
  public HomeController       homeController;
  public FurnitureCatalogTree catalogTree;
  public FurnitureController  furnitureController;
  public FurnitureTable       furnitureTable;


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

    this.triangleRoom = getTriangle();
    this.closeRectangleRoom = getRectangleClose();
    this.farRectangleRoom = getRectangleFar();
    this.weirdRoomPointsOnEdge = getStrangeShape();
    this.weirdRoomPointsOutsideEdge = getStrangeShapeOutside();
    this.weirdCloseButDetached = getStrangeShapeCloseButDeatched();
    
  }

  /**
   * check intersections bween rooms  
   */
  public void testIntersectionFarInner()
  {


    boolean intes =  this.triangleRoom.intersect
        (this.farRectangleRoom, intersectionAlgorithm.INNER_POINTS);

    assertFalse("Inner points, triangle and far rect should not meet" , intes);

  }

  public void testTriangleCloseInner()
  {
    boolean intes =  this.triangleRoom.intersect
        (this.closeRectangleRoom, intersectionAlgorithm.INNER_POINTS);

    assertTrue("Inner points, triangle and close rect should  meet" , intes);

  }


  public void testIntersectionFarArea()
  {

    boolean intes =  this.triangleRoom.intersect
        (this.farRectangleRoom, intersectionAlgorithm.AREA);

    assertFalse("Area, triangle and far rect should not meet" , intes);
  }

  public void testTriangleCloseArea()
  {
    boolean intes =  this.triangleRoom.intersect
        (this.closeRectangleRoom, intersectionAlgorithm.AREA);

    assertTrue("Area, triangle and close rect should  meet" , intes);

  }


  public void testIntersectionCloseStrangeArea()
  {

    boolean intes =  this.triangleRoom.intersect
        (this.weirdRoomPointsOnEdge, intersectionAlgorithm.AREA);

    assertTrue("Area, triangle and strange close area should  meet" , intes);
  }

  public void testIntersectionCloseStrangeInner()
  {

    boolean intes =  this.triangleRoom.intersect
        (this.weirdRoomPointsOnEdge, intersectionAlgorithm.INNER_POINTS);

    assertTrue("Area, triangle and strange close area should  meet" , intes);
  }

  /**
   * !!!!
   * Inner point approach is not general enough
   */
  /*
   *   public void testIntersectionCloseStrangeOutsideEdgeInner()
   *
   * {
   *  
   *  boolean intes =  this.triangleRoom.intersect
   *          (this.weirdRoomPointsOutsideEdge, intersectionAlgorithm.INNER_POINTS);
   *  
   *  assertTrue("Inner, triangle and strange close area with points outside edges should  meet" , intes);
   * }
   */

  public void testIntersectionCloseStrangeOutsideEdgeArea()
  {

    boolean intes =  this.triangleRoom.intersect
        (this.weirdRoomPointsOutsideEdge, intersectionAlgorithm.AREA);

    assertTrue("Area, triangle and strange close area with points outside edges should  meet" , intes);
  }

  public void testIntersectionSlightlyOutsideArea()
  {
   boolean intes = this.triangleRoom.intersect
           (this.weirdCloseButDetached, intersectionAlgorithm.AREA);
   assertFalse("Area, weird room is outside" , intes);
    
  }
  
  
  public static class ControllerTest extends HomeController {
    public ControllerTest(Home home, 
                          UserPreferences preferences,
                          ViewFactory viewFactory) {

      super(home, preferences, viewFactory);
      new ViewTest(this).displayView();
    }
  }
  
  public  static void main(String [] args) {
    ViewFactory viewFactory = new SwingViewFactory();
    UserPreferences preferences = new DefaultUserPreferences();
    preferences.setUnit(LengthUnit.METER);
    Home home = new Home();
    ControllerTest t = new ControllerTest(home, preferences, viewFactory);
    
    RoomsIntersectionsTest rit = new RoomsIntersectionsTest();
    rit.doStuffInsideMain(home, preferences);
    
  }
  

  @Override
  public void doStuffInsideMain(Home home, UserPreferences preferences) {
    RoomGeoSmart r1 = getTriangle();
    RoomGeoSmart r2 = getStrangeShapeCloseButDeatched();
    home.addRoom(r1);
    home.addRoom(r2);

    System.out.println( "intersect : " + r1.intersect(r2, intersectionAlgorithm.INNER_POINTS));

    
  }
}
