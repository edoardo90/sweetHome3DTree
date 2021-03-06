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
import java.awt.geom.Area;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Rectangle3D;
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

  
  
  @Override
  protected void setUp() {

    this.wallAside = getWallAside();
    super.setUp();
    
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
    testIntersection(new Home());
    
  }
  
  public void testIntersection(Home home)
  {
    System.out.println("rect room :\n" + this.rectangularRoom);
    Polygon p =  this.rectangularRoom.getPolygonBigger(100);
    
    RoomGeoSmart rgsm = new RoomGeoSmart(p);
    System.out.println(" room from polygon :\n" + rgsm);
    
    RoomGeoSmart rgArea = new RoomGeoSmart(new Area(p));
    System.out.println("rgArea:\n" + rgArea);
    
    
    RoomGeoSmart shiftedRectangularRoom = this.rectangularRoomShifted;
    shiftedRectangularRoom.move(-400, 0);
    
    home.addRoom(shiftedRectangularRoom);
    home.addRoom(rectangularRoom);
    
    System.out.println(shiftedRectangularRoom);
    RoomGeoSmart intersectionAreaRoom = 
           shiftedRectangularRoom.intersectionAreaRoom(this.rectangularRoom);
    System.out.println("intersection:" + intersectionAreaRoom);
    
    Vector3D centroidTheoretical = new Vector3D(700, 100, 0);
    Vector3D centroidActual = intersectionAreaRoom.getCentroidRegular();
    
    System.out.println(centroidActual);
    
    assertTrue("centroid in wrong position", centroidActual.almostEqual(centroidTheoretical));
 
  }
  
  
  
  
  public void testSeparatingWall()
  {
    boolean wallBwL12 = elRoom1.isTheWallSeparating( elRoom2,       wallBetweenL);
    assertTrue("wall is indeed separating", wallBwL12);
    boolean isw2 =     elRoom1.isTheWallSeparating(squareBelowL1, wallBetweenL1Square);
    assertTrue("wall is indeed separating", isw2);
    
    boolean iswsep = this.isTheWallSeparating
          (rectangularRoom, rectangularRoomShiftedDown, wallAside, home);
    assertFalse("wall is aside, not separating", iswsep);
    
    iswsep = rectangularRoom.isTheWallSeparating
        (rectangularRoomShiftedDown, wallAside);
    assertFalse("wall is aside, not separating", iswsep);
    
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
  
  public boolean isTheWallSeparating(RoomGeoSmart r1 , RoomGeoSmart r2, RoomGeoSmart wall, Home home)
  {
    Area a1 = new Area( r1.getPolygonBigger(100)); //getAreaShape100Big
    Area a2 = new Area( r2.getPolygonBigger(100));
    Rectangle3D rectBounds = wall.getBoundingRoomRect3D();
    float borderSize = (float) rectBounds.getMinEdge();
    System.out.println(borderSize);
    RoomGeoSmart  borderedWall = wall.getBiggerRoomBordered(borderSize);
    
    if(home == null)
      home = new Home();
    home.addRoom(borderedWall);
    
    Area aw = new Area (borderedWall.getPolygonBigger(100));
    
    Area a1AndWall = (Area) a1.clone();
    a1AndWall.intersect(aw);
    
    Area a2AndWall = (Area) a2.clone();
    a2AndWall.intersect(aw);
    
    //now I have the "chunks" of rooms who are interested in the wall 
    //if we imagine the wall covered of fresh paint these (a2AndWall, a1AndWall) would be the dirty of paint areas
    
    RoomGeoSmart roomInters1 = new RoomGeoSmart(a1AndWall);
    RoomGeoSmart roomInters2 = new RoomGeoSmart(a2AndWall);
    
    home.addRoom(roomInters1);
    home.addRoom(roomInters2);
    
    Vector3D centroid1 = roomInters1.getCentroidRegular();
    Vector3D centroid2 = roomInters2.getCentroidRegular();
    
    RoomGeoSmart pointCentroidRoom1 = new RoomGeoSmart(centroid1);
    home.addRoom(pointCentroidRoom1);
    RoomGeoSmart pointCentroidRoom2 = new RoomGeoSmart(centroid2);
    home.addRoom(pointCentroidRoom2);
    
    
    
    Rectangle3D rectWall = wall.getBoundingRoomRect3D();
    Vector3D rowOfPerpendicularSegm1 = rectWall.perpendicularVectorTowardsInside(centroid1);
    Vector3D rowOfPerpendicularSegm2 = rectWall.perpendicularVectorTowardsInside(centroid2);
    
    
    boolean separatingWall = a2AndWall.contains(rowOfPerpendicularSegm1.first * 100, rowOfPerpendicularSegm1.second *100);
    
    List<Segment3D> longSegmList = rectWall.getLongEdges();
    Segment3D seg1 = longSegmList.get(0);
    Segment3D seg2 = longSegmList.get(1);
    
    RoomGeoSmart seg1Room = new RoomGeoSmart(seg1);
    RoomGeoSmart seg2Room = new RoomGeoSmart(seg2);
    
    
    RoomGeoSmart pointRow1 = new RoomGeoSmart(rowOfPerpendicularSegm1);
    RoomGeoSmart pointRow2 = new RoomGeoSmart(rowOfPerpendicularSegm2);
    
    
    boolean confirmIn = a1AndWall.contains(rowOfPerpendicularSegm2.first * 100, rowOfPerpendicularSegm2.second*100);
    
    
    home.addRoom(seg1Room);
    home.addRoom(seg2Room);
    
    home.addRoom(pointRow1);
    home.addRoom(pointRow2);
    
    
    return separatingWall;
  
  }
  
  
  
    
  
  
  

  @Override
  public void doStuffInsideMain(Home home, UserPreferences preferences) {
    
    this.setUp();
    
    this.testIntersection(home);
    
    home.addRoom(rectangularRoom);
    home.addRoom(rectangularRoomShifted);
    
    RoomGeoSmart intersectionAreaRoom = this.rectangularRoomShifted.intersectionAreaRoom(this.rectangularRoom);
    System.out.println(intersectionAreaRoom);

    
    float borderSize = (float) wallBetweenL.getBoundingRoomRect3D().getMinEdge();
    RoomGeoSmart biggerW = wallBetweenL.getBiggerRoomBordered(borderSize/2);
    
    
    boolean isw = elRoom1.isTheWallSeparating( elRoom2, wallBetweenL);
    System.out.println(isw); //ok!
    
    
    boolean isw2 = elRoom1.isTheWallSeparating( elRoom1, wallBetweenL1Square);
    System.out.println(isw2); //ok!
    
    
    boolean iswsep = this.isTheWallSeparating(rectangularRoom, 
                           rectangularRoomShiftedDown, wallAside, home);
    System.out.println(iswsep);

    
  }






    


}
  
  
