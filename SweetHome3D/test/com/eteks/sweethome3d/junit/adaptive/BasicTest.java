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

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectType;
import com.eteks.sweethome3d.adaptive.security.extractingobjs.IfcSecurityExtractor;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Rectangle3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.OBJWriterTest;
import com.eteks.sweethome3d.model.CatalogPieceOfFurniture;
import com.eteks.sweethome3d.model.FurnitureCategory;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.RoomGeoSmart;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.model.Wall;
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
public abstract  class BasicTest extends TestCase {
  public ViewFactory          viewFactory;
  public UserPreferences      preferences;
  public  Home                 home;
  public HomeController       homeController;
  public FurnitureCatalogTree catalogTree;
  public FurnitureController  furnitureController;
  public FurnitureTable       furnitureTable;

  protected RoomGeoSmart     triangleRoom;
  protected RoomGeoSmart     farRectangleRoom;
  protected RoomGeoSmart     closeRectangleRoom;
  protected RoomGeoSmart     weirdRoomPointsOnEdge;
  protected RoomGeoSmart     weirdRoomPointsOutsideEdge;
  protected RoomGeoSmart     weirdCloseButDetached;
  protected RoomGeoSmart     squareRoom;
  protected RoomGeoSmart     rectangularRoom;
  protected RoomGeoSmart        rectangularRoomShifted;
  protected RoomGeoSmart     rectangularRoomShiftedDown;

  protected RoomGeoSmart        elRoom1;
  protected RoomGeoSmart        elRoom2;
  protected RoomGeoSmart        wallBetweenL;
  protected RoomGeoSmart        squareBelowL1;
  protected RoomGeoSmart         wallBetweenL1Square;
  protected RoomGeoSmart wallAside;


  protected RoomGeoSmart cubbyRoom;
  protected RoomGeoSmart diningRoom;

  protected RoomGeoSmart kitchen;
  protected RoomGeoSmart livingRoom;

  protected Wall w1;
  protected Wall w2;
  protected Wall w3;
  protected Wall w4;
  protected Wall w5;


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

    this.rectangularRoom = getRectangularShortRoom(0,0);
    this.rectangularRoomShiftedDown = getRectangularShortRoom(0, 800);
    this.rectangularRoomShifted = getRectangularShortRoom(1000, 0);

    this.squareRoom = getSquareRoom();

    this.rectangularRoom = getRectangularShortRoom(0,0);
    this.elRoom1 = getEl1Room();
    this.elRoom2 = getEl2Room();
    this.wallBetweenL = getWallBetween();
    this.squareBelowL1 = getroomBelowL1();
    this.wallBetweenL1Square = getWallL1SQ();
    this.wallAside = getWallAside();

    this.kitchen = getSquareRoom(100, 100).getBiggerCopyMultiplied(3);
    this.kitchen.move(-300, -80);
    this.kitchen.setId("kitchen");
    this.kitchen.setName("kitchen");

    this.livingRoom = getRectangularShortRoom(240, 0);
    this.livingRoom.setId("livingRoom");
    this.livingRoom.setName("livingRoom");

    this.cubbyRoom = getSquareRoom();
    this.cubbyRoom.setName("cubbyRoom");
    this.cubbyRoom.setId("cubbyRoom");

    this.diningRoom = getSquareRoom();
    this.diningRoom = this.diningRoom.getBiggerCopyMultiplied(3);
    this.diningRoom.move(600, 200);
    this.diningRoom.setId("diningRoom");
    this.diningRoom.setName("diningRoom");



  }

  public BuildingObjectType getTypeFromSweetName(String name)
  {
    ConfigFileEvilTest cfg = ConfigFileEvilTest.getInstance(preferences);
    BuildingObjectType typeOfObject = cfg.getTypeForSweetHomeName(name);
    return typeOfObject; 
  }
  
  public String getSweetNameFromType(BuildingObjectType type)
  {
    ConfigFileEvilTest  cfg = ConfigFileEvilTest.getInstance(preferences);
    String sweetName =  cfg.getSweetHomeNameForType(type);
    return sweetName;
    
  }


  protected void prepareHome(Home home, UserPreferences preferences)
  {

    w1 = new Wall(220,    0,   220,   200,  30,  200);
    w2 = new Wall(0,      0,   1200,  0,    30,  200);
    w3 = new Wall(0,    220,   1200,  220,  30,  200);
    w4 = new Wall(600,  220,   600,   800,  30,  200);
    w5 = new Wall(1200, 220,   1200,  800,  30,  200);


    home.addRoom(this.livingRoom);
    home.addRoom(this.kitchen);
    home.addRoom(this.cubbyRoom);
    home.addRoom(this.diningRoom);
    home.addWall(w1);
    home.addWall(w2);
    home.addWall(w3);
    home.addWall(w4);
    home.addWall(w5);

    HomePieceOfFurniture hopf = getHomePiece(preferences, 1, 4, "washing", 100, 60);
    home.addPieceOfFurniture(hopf);

  }

  protected HomePieceOfFurniture getHomePiece(UserPreferences preferences, int categ, int item, String id, int x, int y)
  {
    FurnitureCategory cat =  preferences.getFurnitureCatalog().getCategories().get(categ);
    List<CatalogPieceOfFurniture> furnitures = cat.getFurniture();

    HomePieceOfFurniture hopf = new HomePieceOfFurniture(furnitures.get(item));
    hopf.setX(x);
    hopf.setY(y);
    hopf.setId(id);
    return hopf;
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
    return this.getSquareRoom(0, 0);
  }

  protected RoomGeoSmart getSquareRoom(int dx, int dy) {

    List<Vector3D> lst = new ArrayList<Vector3D>();

    lst.add(new Vector3D(0   + dx, 0   +  dy, 0));
    lst.add(new Vector3D(200 + dx, 0   +  dy, 0));
    lst.add(new Vector3D(200 + dx, 200 +  dy, 0));
    lst.add(new Vector3D(0   + dx, 200 +  dy, 0));

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

    lst.add(new Vector3D(600, 700, 0)); 
    lst.add(new Vector3D(500,  700, 0));
    lst.add(new Vector3D(500, 500, 0)); 
    lst.add(new Vector3D(600, 500, 0));  

    return new RoomGeoSmart(lst);
  } 

  protected RoomGeoSmart getroomBelowL1() {
    List<Vector3D> lst = new ArrayList<Vector3D>();

    lst.add(new Vector3D(100, 750, 0)); 
    lst.add(new Vector3D(800,  750, 0));
    lst.add(new Vector3D(800, 950, 0)); 
    lst.add(new Vector3D(100, 950, 0));  

    return new RoomGeoSmart(lst);
  }
  protected RoomGeoSmart getWallL1SQ() {
    List<Vector3D> lst = new ArrayList<Vector3D>();

    lst.add(new Vector3D(300, 750, 0)); 
    lst.add(new Vector3D(100, 750, 0));
    lst.add(new Vector3D(100, 700, 0));
    lst.add(new Vector3D(300, 700, 0));  

    return new RoomGeoSmart(lst);
  }

  protected RoomGeoSmart getWallAside() {
    List<Vector3D> lst = new ArrayList<Vector3D>();

    lst.add(new Vector3D(900, 1000, 0)); 
    lst.add(new Vector3D(800, 1000, 0));
    lst.add(new Vector3D(800,  0, 0));
    lst.add(new Vector3D(900, 0, 0));  

    return new RoomGeoSmart(lst);
  }


  public  static void main(String [] args) {
    ViewFactory viewFactory = new SwingViewFactory();
    UserPreferences preferences = new DefaultUserPreferences();
    preferences.setUnit(LengthUnit.METER);
    Home home = new Home();
    ControllerTest t = new ControllerTest(home, preferences, viewFactory);

  }

  public abstract void doStuffInsideMain(Home home, UserPreferences preferences);


  public BuildingSecurityGraph openIfcAndReadIt(Home home, UserPreferences preferences, String name ) throws Exception
  {
    String ifcFileName = "";
    Class<OBJWriterTest> classe =OBJWriterTest.class;
    URL url = classe.getResource("resources/ifcfiles/" + name);
    URI uri = url.toURI();
    File file = new File(uri);
    ifcFileName = file.getAbsolutePath();


    IfcSecurityExtractor extractor= null;

    try{
      extractor = new IfcExtractorScale(ifcFileName, preferences, 1f);
    }
    catch(Exception e)
    {
      System.out.println(e.getStackTrace());
    }

    BuildingSecurityGraph graph = extractor.getGraph();


    IfcSecurityExtractor extractorScaled = new IfcExtractorScale(ifcFileName, preferences, 2f);

    BuildingSecurityGraph graphScaled = extractorScaled.getGraph();
    return graphScaled;

  }

  public BuildingSecurityGraph openIfcAndReadIt(Home home, UserPreferences preferences ) throws Exception
  {
    return this.openIfcAndReadIt(home, preferences, "5 rooms with objects.ifc");
  }




  protected static boolean almostEqual(double a, double b)
  {
    double diff = a - b;
    diff = Math.abs(diff);
    return diff < 10e-06;
  }

  protected static boolean almostEqual(float a , float b)
  {
    return  almostEqual((float)a, (float)b);
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
  protected RoomGeoSmart getRectangleFar()
  {
    Vector3D center = new Vector3D(4000, 4000, 0);
    Rectangle3D rect2 = new Rectangle3D(center, 2000, 5000);

    RoomGeoSmart room2 = new RoomGeoSmart(rect2.getListOfPoints());
    return room2;
  }

  //rect far from triangle
  protected RoomGeoSmart getRectangleClose()
  {
    Vector3D center = new Vector3D(500, 500, 0);
    Rectangle3D rect2 = new Rectangle3D(center, 300, 300);

    RoomGeoSmart room2 = new RoomGeoSmart(rect2.getListOfPoints());
    return room2;
  }


  protected RoomGeoSmart getStrangeShape()
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

  protected RoomGeoSmart getStrangeShapeOutside() {
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

  protected RoomGeoSmart getStrangeShapeCloseButDeatched() {
    List<Vector3D> lst = new ArrayList<Vector3D>();

    lst.add(new Vector3D(110, 100, 0));
    lst.add(new Vector3D(510, 200, 0));
    lst.add(new Vector3D(610, 300, 0));
    lst.add(new Vector3D(610, 500, 0));
    lst.add(new Vector3D(410, 400, 0));

    return new RoomGeoSmart(lst);
  }






}
