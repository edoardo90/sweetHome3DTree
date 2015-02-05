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

import java.awt.Component;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JTable;

import junit.framework.TestCase;

import com.eteks.sweethome3d.adaptive.URLContent;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Rectangle3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.io.HomeFileRecorder;
import com.eteks.sweethome3d.junit.HomeControllerTest;
import com.eteks.sweethome3d.junit.TestUtilities;
import com.eteks.sweethome3d.model.CatalogPieceOfFurniture;
import com.eteks.sweethome3d.model.CollectionEvent;
import com.eteks.sweethome3d.model.CollectionListener;
import com.eteks.sweethome3d.model.Content;
import com.eteks.sweethome3d.model.FurnitureCategory;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomeApplication;
import com.eteks.sweethome3d.model.HomeFurnitureGroup;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.HomeRecorder;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.model.RoomGeoSmart;
import com.eteks.sweethome3d.model.RoomGeoSmart.intersectionAlgorithm;
import com.eteks.sweethome3d.model.Selectable;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.model.Wall;
import com.eteks.sweethome3d.swing.FurnitureCatalogTree;
import com.eteks.sweethome3d.swing.FurnitureTable;
import com.eteks.sweethome3d.swing.HomePane;
import com.eteks.sweethome3d.swing.SwingViewFactory;
import com.eteks.sweethome3d.viewcontroller.FurnitureCatalogController;
import com.eteks.sweethome3d.viewcontroller.FurnitureController;
import com.eteks.sweethome3d.viewcontroller.HomeController;
import com.eteks.sweethome3d.viewcontroller.HomeView;
import com.eteks.sweethome3d.viewcontroller.PlanController;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

/**
 * Tests home controller.
 * @author Emmanuel Puybaret
 */
public class RoomsIntersectionsTest extends TestCase {
  private ViewFactory          viewFactory;
  private UserPreferences      preferences;
  public  Home                 home;
  private HomeController       homeController;
  private FurnitureCatalogTree catalogTree;
  private FurnitureController  furnitureController;
  private FurnitureTable       furnitureTable;

  private RoomGeoSmart     triangleRoom;
  private RoomGeoSmart     farRectangleRoom;
  private RoomGeoSmart     closeRectangleRoom;
  private RoomGeoSmart     weirdRoomPointsOnEdge;
  private RoomGeoSmart     weirdRoomPointsOutsideEdge;
  private RoomGeoSmart     weirdCloseButDetached;

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



  public static void main(String [] args) {
    ViewFactory viewFactory = new SwingViewFactory();
    UserPreferences preferences = new DefaultUserPreferences();
    preferences.setUnit(LengthUnit.METER);
    Home home = new Home();
    ControllerTest t = new ControllerTest(home, preferences, viewFactory);


  }

  private static class ControllerTest extends HomeController {
    public ControllerTest(Home home, 
                          UserPreferences preferences,
                          ViewFactory viewFactory) {

      super(home, preferences, viewFactory);
      new ViewTest(this).displayView();

      RoomGeoSmart r1 = getTriangle();
      RoomGeoSmart r2 = getStrangeShapeCloseButDeatched();
      home.addRoom(r1);
      home.addRoom(r2);

      System.out.println( "intersect : " + r1.intersect(r2, intersectionAlgorithm.INNER_POINTS));


    }
  }




  //triangle
  public  static RoomGeoSmart getTriangle()
  {
    float x1, y1, x2, y2,  x3, y3, x4, y4;
    x1 = 0;
    y1 = 0;

    x2=1000;
    y2=1000;

    x3 = 0;
    y3 = 1000;


    float [][] points = new float[][]{{x1, y1}, {x2, y2}, {x3, y3}};
    RoomGeoSmart r = new RoomGeoSmart(points);
    return r;
  }

  //rect far from triangle
  private static RoomGeoSmart getRectangleFar()
  {
    Vector3D center = new Vector3D(4000, 4000, 0);
    Rectangle3D rect2 = new Rectangle3D(center, 2000, 5000);

    RoomGeoSmart room2 = new RoomGeoSmart(rect2.getListOfPoints());
    return room2;
  }

  //rect far from triangle
  private static RoomGeoSmart getRectangleClose()
  {
    Vector3D center = new Vector3D(500, 500, 0);
    Rectangle3D rect2 = new Rectangle3D(center, 300, 300);

    RoomGeoSmart room2 = new RoomGeoSmart(rect2.getListOfPoints());
    return room2;
  }


  private static RoomGeoSmart getStrangeShape()
  {

    List<Vector3D> lst = new ArrayList<Vector3D>();

    lst.add(new Vector3D(200, 200, 0));
    lst.add(new Vector3D(500, 500, 0));
    //lst.add(new Vector3D(500, 800, 0));
    lst.add(new Vector3D(0, 800, 0));
    lst.add(new Vector3D(-200, 800, 0));
    lst.add(new Vector3D(-500, 600, 0));
    lst.add(new Vector3D(-500, 400, 0));


    return new RoomGeoSmart(lst);
  }

  private static RoomGeoSmart getStrangeShapeOutside() {
    List<Vector3D> lst = new ArrayList<Vector3D>();

    lst.add(new Vector3D(220, 180, 0));
    lst.add(new Vector3D(520, 480, 0));
    //lst.add(new Vector3D(500, 800, 0));
    lst.add(new Vector3D(-20, 820, 0));
    lst.add(new Vector3D(-200, 800, 0));
    lst.add(new Vector3D(-500, 600, 0));
    lst.add(new Vector3D(-500, 400, 0));


    return new RoomGeoSmart(lst);
  }

  private static RoomGeoSmart getStrangeShapeCloseButDeatched() {
    List<Vector3D> lst = new ArrayList<Vector3D>();

    lst.add(new Vector3D(110, 100, 0));
    lst.add(new Vector3D(510, 200, 0));
    lst.add(new Vector3D(610, 300, 0));
    lst.add(new Vector3D(610, 500, 0));
    lst.add(new Vector3D(410, 400, 0));
    
    return new RoomGeoSmart(lst);
  }
  
  

  private static class ViewTest extends JRootPane {
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
}
