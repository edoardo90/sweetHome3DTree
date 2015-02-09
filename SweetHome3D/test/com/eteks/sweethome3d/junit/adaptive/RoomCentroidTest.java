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

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Segment3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.LengthUnit;
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
 * @author Edoardo Pasi
 */
public class RoomCentroidTest extends BasicTest {
  public ViewFactory          viewFactory;
  public UserPreferences      preferences;
  public  Home                 home;
  public HomeController       homeController;
  public FurnitureCatalogTree catalogTree;
  public FurnitureController  furnitureController;
  public FurnitureTable       furnitureTable;

  private RoomGeoSmart        squareRoom;
  private RoomGeoSmart        rectangularRoom;
  private RoomGeoSmart        rectangularRoomShifted;
  private RoomGeoSmart        elRoom1;
  private RoomGeoSmart        elRoom2;
  private RoomGeoSmart        wallBetweenL;

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
    
    this.squareRoom = getSquareRoom();
    this.rectangularRoom = getRectangularShortRoom(0,0);
    this.rectangularRoomShifted = getRectangularShortRoom(300, 0);
    
  }


  public void  testCentroidSquare()
  {
    
    Vector3D centroid = this.squareRoom.getCentroid100Big();
    
    System.out.println("centroid:" +  centroid);
    
    centroid.scale(0.01f);
    
    System.out.println("centroid:" +  centroid);
    
    assertTrue("wrong x:" ,  almostEqual(centroid.first, 100f));
    assertTrue("wrong y:" ,  almostEqual(centroid.second, 100f));
    
  }
  

  public void  testCentroidRect()
  {
    
    Vector3D centroid = this.rectangularRoom.getCentroid100Big();
    
    System.out.println("centroid:" +  centroid);
    
    centroid.scale(0.01f);
    
    System.out.println("centroid:" +  centroid);
    
    assertTrue("wrong x:" ,  almostEqual(centroid.first, 400f));
    assertTrue("wrong y:" ,  almostEqual(centroid.second, 100f));
    
  }
  
  public void testIntersection()
  {
    
    System.out.println("rect room :\n" + this.rectangularRoom);
    Polygon p =  this.rectangularRoom.getPolygon();
    
    RoomGeoSmart rgsm = new RoomGeoSmart(p);
    System.out.println(" room from polygon :\n" + rgsm);
    
    RoomGeoSmart rgArea = new RoomGeoSmart(new Area(p));
    System.out.println("rgArea:\n" + rgArea);
    
   
    RoomGeoSmart shiftedRectangularRoom = this.rectangularRoomShifted;
    RoomGeoSmart intersectionAreaRoom = shiftedRectangularRoom.intersectionAreaRoom(this.rectangularRoom);
    System.out.println(intersectionAreaRoom);
    
    Vector3D centroidTheoretical = new Vector3D(550, 100, 0);
    Vector3D centroidActual = intersectionAreaRoom.getCentroidRegular();
    
    System.out.println(centroidActual);
    
    assertTrue("centroid in wrong position", centroidActual.almostEqual(centroidTheoretical));
    
  }
  
  
  
  

  protected RoomGeoSmart getRectangularShortRoom(int dx, int dy) {
    List<Vector3D> lst = new ArrayList<Vector3D>();

    lst.add(new Vector3D(0    +dx,     0    +dy,      0));
    lst.add(new Vector3D(800  +dx,     0    +dy,      0));
    lst.add(new Vector3D(800  +dx,     200  +dy,  0));
    lst.add(new Vector3D(0    +dx,     200  +dy,  0));
    
    return new RoomGeoSmart(lst);
  }
  
  
  
  protected RoomGeoSmart getSquareRoom() {
    List<Vector3D> lst = new ArrayList<Vector3D>();

    lst.add(new Vector3D(0, 0, 0));
    lst.add(new Vector3D(200, 0, 0));
    lst.add(new Vector3D(200, 200, 0));
    lst.add(new Vector3D(0, 200, 0));
    
    return new RoomGeoSmart(lst);
  }
  
  protected RoomGeoSmart getEl1Room()
  {
    List<Vector3D> lst = new ArrayList<Vector3D>();

    lst.add(new Vector3D(0, 0, 0));
    lst.add(new Vector3D(200, 0, 0));
    lst.add(new Vector3D(200, 500, 0));
    lst.add(new Vector3D(500, 500, 0));
    lst.add(new Vector3D(500, 700, 0));
    lst.add(new Vector3D(0, 700, 0));    
    
    return new RoomGeoSmart(lst);
  }
  
  protected RoomGeoSmart getEl2Room()
  {
    List<Vector3D> lst = new ArrayList<Vector3D>();

    
    lst.add(new Vector3D(1000, 0, 0));
    lst.add(new Vector3D(1200, 0, 0));
    lst.add(new Vector3D(1200, 700, 0));
    lst.add(new Vector3D(600,  700, 0));
    lst.add(new Vector3D(600, 500, 0));
    lst.add(new Vector3D(1000, 500, 0));    
    
    return new RoomGeoSmart(lst);
    
    
  }
  
  protected RoomGeoSmart getWallBetween() {
    List<Vector3D> lst = new ArrayList<Vector3D>();

    
    lst.add(new Vector3D(500, 500, 0));
    lst.add(new Vector3D(600, 500, 0));
    lst.add(new Vector3D(600, 700, 0));
    lst.add(new Vector3D(500,  700, 0));
    
    return new RoomGeoSmart(lst);
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
    
    RoomCentroidTest rit = new RoomCentroidTest();
    rit.doStuffInsideMain(home, preferences);
    
  }
  

  

  @Override
  public void doStuffInsideMain(Home home, UserPreferences preferences) {
    
    this.rectangularRoom = getRectangularShortRoom(0,0);
    this.rectangularRoomShifted = getRectangularShortRoom(300, 0);
    this.elRoom1 = getEl1Room();
    this.elRoom2 = getEl2Room();
    this.wallBetweenL = getWallBetween();
    
    RoomGeoSmart intersectionAreaRoom = this.rectangularRoomShifted.intersectionAreaRoom(this.rectangularRoom);
    
    System.out.println(intersectionAreaRoom);
    
    home.addRoom(elRoom1);
    home.addRoom(elRoom2);
    home.addRoom(wallBetweenL);
    
    boolean isw = elRoom1.isTheWallSeparating(elRoom2, wallBetweenL);
    System.out.println(isw);
    
  }

    


}
  
  
