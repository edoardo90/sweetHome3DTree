/*
 * Room.java 18 nov. 2008
 *
 * Sweet Home 3D, Copyright (c) 2008 Emmanuel PUYBARET / eTeks <info@eteks.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.eteks.sweethome3d.model;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

/**
 * A room or a polygon in a home plan. 
 * @author Emmanuel Puybaret
 */
public class Room implements Serializable, Selectable, Elevatable {


  /**
   * The properties of a room that may change. <code>PropertyChangeListener</code>s added 
   * to a room will be notified under a property name equal to the string value of one these properties.
   */
  public enum Property {NAME, NAME_X_OFFSET, NAME_Y_OFFSET, NAME_STYLE, NAME_ANGLE,
    POINTS, AREA_VISIBLE, AREA_X_OFFSET, AREA_Y_OFFSET, AREA_STYLE, AREA_ANGLE,
    FLOOR_COLOR, FLOOR_TEXTURE, FLOOR_VISIBLE, FLOOR_SHININESS,
    CEILING_COLOR, CEILING_TEXTURE, CEILING_VISIBLE, CEILING_SHININESS, LEVEL}

  private static final long serialVersionUID = 1L;

  private static final double TWICE_PI = 2 * Math.PI;

  private String      name;
  private float       nameXOffset;
  private float       nameYOffset;
  private TextStyle   nameStyle;
  private float       nameAngle;
  private float [][]  points;
  private boolean     areaVisible;
  private float       areaXOffset;
  private float       areaYOffset;
  private TextStyle   areaStyle;
  private float       areaAngle;
  private boolean     floorVisible;
  private Integer     floorColor;
  private HomeTexture floorTexture;
  private float       floorShininess;
  private boolean     ceilingVisible;
  private Integer     ceilingColor;
  private HomeTexture ceilingTexture;
  private float       ceilingShininess;
  private Level       level;

  private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
  private transient Shape shapeCache;
  private transient Float areaCache;

  /**
   * Creates a room from its name and the given coordinates.
   */
  public Room(float [][] points) {


    if (points.length <= 1) {
      throw new IllegalStateException("Room points must containt at least two points");
    }
    this.points = deepCopy(points);
    this.areaVisible = true;
    this.nameYOffset = -40f;
    this.floorVisible = true;
    this.ceilingVisible = true;
  }

  public Room(List<Vector3D> points)
  {

    if (points == null || points.size() <= 1) {
      throw new IllegalStateException("Room points must containt at least two points");
    }

    float x1 = (float)points.get(0).first;
    float y1 = (float)points.get(0).second;
    float x2 = (float)points.get(1).first;
    float y2 = (float)points.get(1).second;

    this.points = new float [][] {{x1, y1}, {x2, y2}}; 
    this.areaVisible = true;
    this.nameYOffset = -40f;
    this.floorVisible = true;
    this.ceilingVisible = true;

  }


  @Override
  public String toString() {
    return this.name == null ?  "Room " + Float.toString(this.areaCache / 10000).substring(0, 1)
        :   this.getName();
  }





  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Float.floatToIntBits(this.areaAngle);
    result = prime * result + ((this.areaStyle == null)
        ? 0
            : this.areaStyle.hashCode());
    result = prime * result + (this.areaVisible
        ? 1231
            : 1237);
    result = prime * result + Float.floatToIntBits(this.areaXOffset);
    result = prime * result + Float.floatToIntBits(this.areaYOffset);
    result = prime * result + ((this.ceilingColor == null)
        ? 0
            : this.ceilingColor.hashCode());
    result = prime * result + Float.floatToIntBits(this.ceilingShininess);
    result = prime * result + ((this.ceilingTexture == null)
        ? 0
            : this.ceilingTexture.hashCode());
    result = prime * result + (this.ceilingVisible
        ? 1231
            : 1237);
    result = prime * result + ((this.floorColor == null)
        ? 0
            : this.floorColor.hashCode());
    result = prime * result + Float.floatToIntBits(this.floorShininess);
    result = prime * result + ((this.floorTexture == null)
        ? 0
            : this.floorTexture.hashCode());
    result = prime * result + (this.floorVisible
        ? 1231
            : 1237);
    result = prime * result + ((this.level == null)
        ? 0
            : this.level.hashCode());
    result = prime * result + ((this.name == null)
        ? 0
            : this.name.hashCode());
    result = prime * result + Float.floatToIntBits(this.nameAngle);
    result = prime * result + ((this.nameStyle == null)
        ? 0
            : this.nameStyle.hashCode());
    result = prime * result + Float.floatToIntBits(this.nameXOffset);
    result = prime * result + Float.floatToIntBits(this.nameYOffset);
    result = prime * result + Arrays.hashCode(this.points);
    return result;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Room)) {
      return false;
    }
    Room other = (Room)obj;
    if (Float.floatToIntBits(this.areaAngle) != Float.floatToIntBits(other.areaAngle)) {
      return false;
    }
    if (this.areaStyle == null) {
      if (other.areaStyle != null) {
        return false;
      }
    } else if (!this.areaStyle.equals(other.areaStyle)) {
      return false;
    }
    if (this.areaVisible != other.areaVisible) {
      return false;
    }
    if (Float.floatToIntBits(this.areaXOffset) != Float.floatToIntBits(other.areaXOffset)) {
      return false;
    }
    if (Float.floatToIntBits(this.areaYOffset) != Float.floatToIntBits(other.areaYOffset)) {
      return false;
    }
    if (this.ceilingColor == null) {
      if (other.ceilingColor != null) {
        return false;
      }
    } else if (!this.ceilingColor.equals(other.ceilingColor)) {
      return false;
    }
    if (Float.floatToIntBits(this.ceilingShininess) != Float.floatToIntBits(other.ceilingShininess)) {
      return false;
    }
    if (this.ceilingTexture == null) {
      if (other.ceilingTexture != null) {
        return false;
      }
    } else if (!this.ceilingTexture.equals(other.ceilingTexture)) {
      return false;
    }
    if (this.ceilingVisible != other.ceilingVisible) {
      return false;
    }
    if (this.floorColor == null) {
      if (other.floorColor != null) {
        return false;
      }
    } else if (!this.floorColor.equals(other.floorColor)) {
      return false;
    }
    if (Float.floatToIntBits(this.floorShininess) != Float.floatToIntBits(other.floorShininess)) {
      return false;
    }
    if (this.floorTexture == null) {
      if (other.floorTexture != null) {
        return false;
      }
    } else if (!this.floorTexture.equals(other.floorTexture)) {
      return false;
    }
    if (this.floorVisible != other.floorVisible) {
      return false;
    }
    if (this.level == null) {
      if (other.level != null) {
        return false;
      }
    } else if (!this.level.equals(other.level)) {
      return false;
    }
    if (this.name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!this.name.equals(other.name)) {
      return false;
    }
    if (Float.floatToIntBits(this.nameAngle) != Float.floatToIntBits(other.nameAngle)) {
      return false;
    }
    if (this.nameStyle == null) {
      if (other.nameStyle != null) {
        return false;
      }
    } else if (!this.nameStyle.equals(other.nameStyle)) {
      return false;
    }
    if (Float.floatToIntBits(this.nameXOffset) != Float.floatToIntBits(other.nameXOffset)) {
      return false;
    }
    if (Float.floatToIntBits(this.nameYOffset) != Float.floatToIntBits(other.nameYOffset)) {
      return false;
    }
    if (!Arrays.deepEquals(this.points, other.points)) {
      return false;
    }
    return true;
  }


  /**
   * Initializes new room transient fields  
   * and reads room from <code>in</code> stream with default reading method.
   */
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    this.propertyChangeSupport = new PropertyChangeSupport(this);
    in.defaultReadObject();
  }

  /**
   * Adds the property change <code>listener</code> in parameter to this wall.
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    this.propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * Removes the property change <code>listener</code> in parameter from this wall.
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    this.propertyChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * Returns the name of this room.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name of this room. Once this room is updated, 
   * listeners added to this room will receive a change notification.
   */
  public void setName(String name) {
    if (name != this.name
        && (name == null || !name.equals(this.name))) {
      String oldName = this.name;
      this.name = name;
      this.propertyChangeSupport.firePropertyChange(Property.NAME.name(), oldName, name);
    }
  }

  /**
   * Returns the distance along x axis applied to room center abscissa 
   * to display room name. 
   */
  public float getNameXOffset() {
    return this.nameXOffset;  
  }

  /**
   * Sets the distance along x axis applied to room center abscissa to display room name. 
   * Once this room  is updated, listeners added to this room will receive a change notification.
   */
  public void setNameXOffset(float nameXOffset) {
    if (nameXOffset != this.nameXOffset) {
      float oldNameXOffset = this.nameXOffset;
      this.nameXOffset = nameXOffset;
      this.propertyChangeSupport.firePropertyChange(Property.NAME_X_OFFSET.name(), oldNameXOffset, nameXOffset);
    }
  }

  /**
   * Returns the distance along y axis applied to room center ordinate 
   * to display room name.
   */
  public float getNameYOffset() {
    return this.nameYOffset;  
  }

  /**
   * Sets the distance along y axis applied to room center ordinate to display room name. 
   * Once this room is updated, listeners added to this room will receive a change notification.
   */
  public void setNameYOffset(float nameYOffset) {
    if (nameYOffset != this.nameYOffset) {
      float oldNameYOffset = this.nameYOffset;
      this.nameYOffset = nameYOffset;
      this.propertyChangeSupport.firePropertyChange(Property.NAME_Y_OFFSET.name(), oldNameYOffset, nameYOffset);
    }
  }

  /**
   * Returns the text style used to display room name.
   */
  public TextStyle getNameStyle() {
    return this.nameStyle;  
  }

  /**
   * Sets the text style used to display room name.
   * Once this room is updated, listeners added to this room will receive a change notification.
   */
  public void setNameStyle(TextStyle nameStyle) {
    if (nameStyle != this.nameStyle) {
      TextStyle oldNameStyle = this.nameStyle;
      this.nameStyle = nameStyle;
      this.propertyChangeSupport.firePropertyChange(Property.NAME_STYLE.name(), oldNameStyle, nameStyle);
    }
  }

  /**
   * Returns the angle in radians used to display the room name.
   * @since 3.6 
   */
  public float getNameAngle() {
    return this.nameAngle;
  }

  /**
   * Sets the angle in radians used to display the room name. Once this piece is updated, 
   * listeners added to this piece will receive a change notification.
   * @since 3.6 
   */
  public void setNameAngle(float nameAngle) {
    // Ensure angle is always positive and between 0 and 2 PI
    nameAngle = (float)((nameAngle % TWICE_PI + TWICE_PI) % TWICE_PI);
    if (nameAngle != this.nameAngle) {
      float oldNameAngle = this.nameAngle;
      this.nameAngle = nameAngle;
      this.propertyChangeSupport.firePropertyChange(Property.NAME_ANGLE.name(), oldNameAngle, nameAngle);
    }
  }

  /**
   * Returns the points of the polygon matching this room. 
   * @return an array of the (x,y) coordinates of the room points.
   */
  public float [][] getPoints() {
    return deepCopy(this.points);  
  }

  public Polygon getPolygon()
  {
    float [][] roomPoints = deepCopy(this.points);
    int [] xpoints = getXPoints( roomPoints, 1);
    int [] ypoints = getYPoints( roomPoints, 1);
    return  new Polygon(xpoints, ypoints, this.getPointCount());
  }

  public Rectangle getRectangleGrowth(int amount)
  {
    Polygon p = this.getPolygon();
    Rectangle r = p.getBounds();
    r.grow(amount, amount);
    return r;
  }

  public boolean intersectApprox(Room r2, int approxTollerance)
  {
    Rectangle rr2 = r2.getRectangleGrowth(approxTollerance);
    Rectangle rr1 = this.getRectangleGrowth(approxTollerance);
    return rr1.intersects(rr2);


  }

  public Polygon getPolygon1000xBigger()
  {
    float [][] roomPoints = deepCopy(this.points);
    int [] xpoints = getXPoints( roomPoints, 1000);
    int [] ypoints = getYPoints( roomPoints, 1000);
    return  new Polygon(xpoints, ypoints, this.getPointCount());
  }

  private int [] getXPoints(float [][] roomPoints, int multiplier)
  {

    int [] xpoints  = new int  [ (roomPoints.length) ];
    float x =0;

    for(int coppia = 0; coppia < roomPoints.length; coppia++)
    {
      x = roomPoints[coppia][0];
      xpoints[coppia] = (int) (x * multiplier);

    }
    return xpoints;
  }

  private int [] getYPoints(float [][] roomPoints,  int multiplier)
  {

    int [] ypoints  = new int  [ (roomPoints.length) ];
    float y;
    for(int coppia = 0; coppia < roomPoints.length; coppia++)
    {
      y =  roomPoints[coppia][1];
      ypoints[coppia] = (int) (y * multiplier);

    }
    return ypoints;
  }



  /**
   * Returns the number of points of the polygon matching this room.
   * @since 2.0 
   */
  public int getPointCount() {
    return this.points.length;  
  }

  private float [][] deepCopy(float [][] points) {
    float [][] pointsCopy = new float [points.length][];
    for (int i = 0; i < points.length; i++) {
      pointsCopy [i] = points [i].clone();
    }
    return pointsCopy;
  }

  /**
   * Sets the points of the polygon matching this room. Once this room 
   * is updated, listeners added to this room will receive a change notification.
   */
  public void setPoints(float [][] points) {
    if (!Arrays.deepEquals(this.points, points)) {
      updatePoints(points);
    }
  }

  /**
   * Update the points of the polygon matching this room.
   */
  private void updatePoints(float [][] points) {
    float [][] oldPoints = this.points;
    this.points = deepCopy(points);
    this.shapeCache = null;
    this.areaCache  = null;
    this.propertyChangeSupport.firePropertyChange(Property.POINTS.name(), oldPoints, points);
  }

  /**
   * Adds a point at the end of room points.
   * @since 2.0
   */
  public void addPoint(float x, float y) {
    addPoint(x, y, this.points.length);
  }

  /**
   * Adds a point at the given <code>index</code>.
   * @throws IndexOutOfBoundsException if <code>index</code> is negative or > <code>getPointCount()</code> 
   * @since 2.0
   */
  public void addPoint(float x, float y, int index) {
    if (index < 0 || index > this.points.length) {
      throw new IndexOutOfBoundsException("Invalid index " + index);
    }

    float [][] newPoints = new float [this.points.length + 1][];
    System.arraycopy(this.points, 0, newPoints, 0, index);
    newPoints [index] = new float [] {x, y};
    System.arraycopy(this.points, index, newPoints, index + 1, this.points.length - index);

    float [][] oldPoints = this.points;
    this.points = newPoints;
    this.shapeCache = null;
    this.areaCache  = null;
    this.propertyChangeSupport.firePropertyChange(Property.POINTS.name(), oldPoints, deepCopy(this.points));
  }

  /**
   * Sets the point at the given <code>index</code>.
   * @throws IndexOutOfBoundsException if <code>index</code> is negative or >= <code>getPointCount()</code> 
   * @since 2.0
   */
  public void setPoint(float x, float y, int index) {
    if (index < 0 || index >= this.points.length) {
      throw new IndexOutOfBoundsException("Invalid index " + index);
    }
    if (this.points [index][0] != x 
        || this.points [index][1] != y) {
      float [][] oldPoints = this.points;
      this.points = deepCopy(this.points);
      this.points [index][0] = x;
      this.points [index][1] = y;
      this.shapeCache = null;
      this.areaCache  = null;
      this.propertyChangeSupport.firePropertyChange(Property.POINTS.name(), oldPoints, deepCopy(this.points));
    }
  }

  /**
   * Removes the point at the given <code>index</code>.
   * @throws IndexOutOfBoundsException if <code>index</code> is negative or >= <code>getPointCount()</code> 
   * @since 2.0
   */
  public void removePoint(int index) {
    if (index < 0 || index >= this.points.length) {
      throw new IndexOutOfBoundsException("Invalid index " + index);
    } else if (this.points.length <= 1) {
      throw new IllegalStateException("Room points must containt at least one point");
    }

    float [][] newPoints = new float [this.points.length - 1][];
    System.arraycopy(this.points, 0, newPoints, 0, index);
    System.arraycopy(this.points, index + 1, newPoints, index, this.points.length - index - 1);

    float [][] oldPoints = this.points;
    this.points = newPoints;
    this.shapeCache = null;
    this.areaCache  = null;
    this.propertyChangeSupport.firePropertyChange(Property.POINTS.name(), oldPoints, deepCopy(this.points));
  }

  /**
   * Returns whether the area of this room is visible or not. 
   */
  public boolean isAreaVisible() {
    return this.areaVisible;  
  }

  /**
   * Sets whether the area of this room is visible or not. Once this room 
   * is updated, listeners added to this room will receive a change notification.
   */
  public void setAreaVisible(boolean areaVisible) {
    if (areaVisible != this.areaVisible) {
      this.areaVisible = areaVisible;
      this.propertyChangeSupport.firePropertyChange(Property.AREA_VISIBLE.name(), !areaVisible, areaVisible);
    }
  }

  /**
   * Returns the distance along x axis applied to room center abscissa 
   * to display room area. 
   */
  public float getAreaXOffset() {
    return this.areaXOffset;  
  }

  /**
   * Sets the distance along x axis applied to room center abscissa to display room area. 
   * Once this room  is updated, listeners added to this room will receive a change notification.
   */
  public void setAreaXOffset(float areaXOffset) {
    if (areaXOffset != this.areaXOffset) {
      float oldAreaXOffset = this.areaXOffset;
      this.areaXOffset = areaXOffset;
      this.propertyChangeSupport.firePropertyChange(Property.AREA_X_OFFSET.name(), oldAreaXOffset, areaXOffset);
    }
  }

  /**
   * Returns the distance along y axis applied to room center ordinate 
   * to display room area.
   */
  public float getAreaYOffset() {
    return this.areaYOffset;  
  }

  /**
   * Sets the distance along y axis applied to room center ordinate to display room area. 
   * Once this room is updated, listeners added to this room will receive a change notification.
   */
  public void setAreaYOffset(float areaYOffset) {
    if (areaYOffset != this.areaYOffset) {
      float oldAreaYOffset = this.areaYOffset;
      this.areaYOffset = areaYOffset;
      this.propertyChangeSupport.firePropertyChange(Property.AREA_Y_OFFSET.name(), oldAreaYOffset, areaYOffset);
    }
  }

  /**
   * Returns the text style used to display room area.
   */
  public TextStyle getAreaStyle() {
    return this.areaStyle;  
  }

  /**
   * Sets the text style used to display room area.
   * Once this room is updated, listeners added to this room will receive a change notification.
   */
  public void setAreaStyle(TextStyle areaStyle) {
    if (areaStyle != this.areaStyle) {
      TextStyle oldAreaStyle = this.areaStyle;
      this.areaStyle = areaStyle;
      this.propertyChangeSupport.firePropertyChange(Property.AREA_STYLE.name(), oldAreaStyle, areaStyle);
    }
  }

  /**
   * Returns the angle in radians used to display the room area.
   * @since 3.6 
   */
  public float getAreaAngle() {
    return this.areaAngle;
  }

  /**
   * Sets the angle in radians used to display the room area. Once this piece is updated, 
   * listeners added to this piece will receive a change notification.
   * @since 3.6 
   */
  public void setAreaAngle(float areaAngle) {
    // Ensure angle is always positive and between 0 and 2 PI
    areaAngle = (float)((areaAngle % TWICE_PI + TWICE_PI) % TWICE_PI);
    if (areaAngle != this.areaAngle) {
      float oldAreaAngle = this.areaAngle;
      this.areaAngle = areaAngle;
      this.propertyChangeSupport.firePropertyChange(Property.AREA_ANGLE.name(), oldAreaAngle, areaAngle);
    }
  }

  /**
   * Returns the abscissa of the center point of this room.
   */
  public float getXCenter() {
    float xMin = this.points [0][0]; 
    float xMax = this.points [0][0]; 
    for (int i = 1; i < this.points.length; i++) {
      xMin = Math.min(xMin, this.points [i][0]);
      xMax = Math.max(xMax, this.points [i][0]);
    }
    return (xMin + xMax) / 2;
  }

  /**
   * Returns the ordinate of the center point of this room.
   */
  public float getYCenter() {
    float yMin = this.points [0][1]; 
    float yMax = this.points [0][1]; 
    for (int i = 1; i < this.points.length; i++) {
      yMin = Math.min(yMin, this.points [i][1]);
      yMax = Math.max(yMax, this.points [i][1]);
    }
    return (yMin + yMax) / 2;
  }

  /**
   * Returns the floor color of this room. 
   */
  public Integer getFloorColor() {
    return this.floorColor;
  }

  /**
   * Sets the floor color of this room. Once this room is updated, 
   * listeners added to this room will receive a change notification.
   */
  public void setFloorColor(Integer floorColor) {
    if (floorColor != this.floorColor
        && (floorColor == null || !floorColor.equals(this.floorColor))) {
      Integer oldFloorColor = this.floorColor;
      this.floorColor = floorColor;
      this.propertyChangeSupport.firePropertyChange(Property.FLOOR_COLOR.name(), 
          oldFloorColor, floorColor);
    }
  }

  /**
   * Returns the floor texture of this room.
   */
  public HomeTexture getFloorTexture() {
    return this.floorTexture;
  }

  /**
   * Sets the floor texture of this room. Once this room is updated, 
   * listeners added to this room will receive a change notification.
   */
  public void setFloorTexture(HomeTexture floorTexture) {
    if (floorTexture != this.floorTexture
        && (floorTexture == null || !floorTexture.equals(this.floorTexture))) {
      HomeTexture oldFloorTexture = this.floorTexture;
      this.floorTexture = floorTexture;
      this.propertyChangeSupport.firePropertyChange(Property.FLOOR_TEXTURE.name(), 
          oldFloorTexture, floorTexture);
    }
  }

  /**
   * Returns whether the floor of this room is visible or not. 
   */
  public boolean isFloorVisible() {
    return this.floorVisible;  
  }

  /**
   * Sets whether the floor of this room is visible or not. Once this room 
   * is updated, listeners added to this room will receive a change notification.
   */
  public void setFloorVisible(boolean floorVisible) {
    if (floorVisible != this.floorVisible) {
      this.floorVisible = floorVisible;
      this.propertyChangeSupport.firePropertyChange(Property.FLOOR_VISIBLE.name(), !floorVisible, floorVisible);
    }
  }

  /**
   * Returns the floor shininess of this room. 
   * @return a value between 0 (matt) and 1 (very shiny)  
   * @since 3.0
   */
  public float getFloorShininess() {
    return this.floorShininess;
  }

  /**
   * Sets the floor shininess of this room. Once this room is updated, 
   * listeners added to this room will receive a change notification.
   * @since 3.0
   */
  public void setFloorShininess(float floorShininess) {
    if (floorShininess != this.floorShininess) {
      float oldFloorShininess = this.floorShininess;
      this.floorShininess = floorShininess;
      this.propertyChangeSupport.firePropertyChange(Property.FLOOR_SHININESS.name(), 
          oldFloorShininess, floorShininess);
    }
  }

  /**
   * Returns the ceiling color color of this room. 
   */
  public Integer getCeilingColor() {
    return this.ceilingColor;
  }

  /**
   * Sets the ceiling color of this room. Once this room is updated, 
   * listeners added to this room will receive a change notification.
   */
  public void setCeilingColor(Integer ceilingColor) {
    if (ceilingColor != this.ceilingColor
        && (ceilingColor == null || !ceilingColor.equals(this.ceilingColor))) {
      Integer oldCeilingColor = this.ceilingColor;
      this.ceilingColor = ceilingColor;
      this.propertyChangeSupport.firePropertyChange(Property.CEILING_COLOR.name(), 
          oldCeilingColor, ceilingColor);
    }
  }

  /**
   * Returns the ceiling texture of this room.
   */
  public HomeTexture getCeilingTexture() {
    return this.ceilingTexture;
  }

  /**
   * Sets the ceiling texture of this room. Once this room is updated, 
   * listeners added to this room will receive a change notification.
   */
  public void setCeilingTexture(HomeTexture ceilingTexture) {
    if (ceilingTexture != this.ceilingTexture
        && (ceilingTexture == null || !ceilingTexture.equals(this.ceilingTexture))) {
      HomeTexture oldCeilingTexture = this.ceilingTexture;
      this.ceilingTexture = ceilingTexture;
      this.propertyChangeSupport.firePropertyChange(Property.CEILING_TEXTURE.name(), 
          oldCeilingTexture, ceilingTexture);
    }
  }

  /**
   * Returns whether the ceiling of this room is visible or not. 
   */
  public boolean isCeilingVisible() {
    return this.ceilingVisible;  
  }

  /**
   * Sets whether the ceiling of this room is visible or not. Once this room 
   * is updated, listeners added to this room will receive a change notification.
   */
  public void setCeilingVisible(boolean ceilingVisible) {
    if (ceilingVisible != this.ceilingVisible) {
      this.ceilingVisible = ceilingVisible;
      this.propertyChangeSupport.firePropertyChange(Property.CEILING_VISIBLE.name(), !ceilingVisible, ceilingVisible);
    }
  }

  /**
   * Returns the ceiling shininess of this room.
   * @return a value between 0 (matt) and 1 (very shiny)  
   * @since 3.0
   */
  public float getCeilingShininess() {
    return this.ceilingShininess;
  }

  /**
   * Sets the ceiling shininess of this room. Once this room is updated, 
   * listeners added to this room will receive a change notification.
   * @since 3.0
   */
  public void setCeilingShininess(float ceilingShininess) {
    if (ceilingShininess != this.ceilingShininess) {
      float oldCeilingShininess = this.ceilingShininess;
      this.ceilingShininess = ceilingShininess;
      this.propertyChangeSupport.firePropertyChange(Property.CEILING_SHININESS.name(), 
          oldCeilingShininess, ceilingShininess);
    }
  }

  /**
   * Returns the level which this room belongs to. 
   * @since 3.4
   */
  public Level getLevel() {
    return this.level;
  }

  /**
   * Sets the level of this room. Once this room is updated, 
   * listeners added to this room will receive a change notification.
   * @since 3.4
   */
  public void setLevel(Level level) {
    if (level != this.level) {
      Level oldLevel = this.level;
      this.level = level;
      this.propertyChangeSupport.firePropertyChange(Property.LEVEL.name(), oldLevel, level);
    }
  }

  /**
   * Returns <code>true</code> if this room is at the given level.
   * @since 3.4
   */
  public boolean isAtLevel(Level level) {
    return this.level == level;
  }

  /**
   * Returns the area of this room.
   */
  public float getArea() {
    if (this.areaCache == null) {
      Area roomArea = new Area(getShape());
      if (roomArea.isSingular()) {
        this.areaCache = Math.abs(getSignedArea(getPoints()));
      } else {
        // Add the surface of the different polygons of this room
        float area = 0;
        List<float []> currentPathPoints = new ArrayList<float[]>();
        for (PathIterator it = roomArea.getPathIterator(null); !it.isDone(); ) {
          float [] roomPoint = new float[2];
          switch (it.currentSegment(roomPoint)) {
            case PathIterator.SEG_MOVETO : 
              currentPathPoints.add(roomPoint);
              break;
            case PathIterator.SEG_LINETO : 
              currentPathPoints.add(roomPoint);
              break;
            case PathIterator.SEG_CLOSE :
              float [][] pathPoints = 
              currentPathPoints.toArray(new float [currentPathPoints.size()][]);
              area += Math.abs(getSignedArea(pathPoints));
              currentPathPoints.clear();
              break;
          }
          it.next();        
        }
        this.areaCache = area;
      }
    }
    return this.areaCache;
  }

  private float getSignedArea(float areaPoints [][]) {
    // From "Area of a General Polygon" algorithm described in  
    // http://www.davidchandler.com/AreaOfAGeneralPolygon.pdf
    float area = 0;
    for (int i = 1; i < areaPoints.length; i++) {
      area += areaPoints [i][0] * areaPoints [i - 1][1];
      area -= areaPoints [i][1] * areaPoints [i - 1][0];
    }
    area += areaPoints [0][0] * areaPoints [areaPoints.length - 1][1];
    area -= areaPoints [0][1] * areaPoints [areaPoints.length - 1][0];
    return area / 2;
  }

  /**
   * Returns <code>true</code> if the points of this room are in clockwise order.
   */
  public boolean isClockwise() {
    return getSignedArea(getPoints()) < 0;
  }

  /**
   * Returns <code>true</code> if this room is comprised of only one polygon.
   */
  public boolean isSingular() {
    return new Area(getShape()).isSingular();
  }

  /**
   * Returns <code>true</code> if this room intersects
   * with the horizontal rectangle which opposite corners are at points
   * (<code>x0</code>, <code>y0</code>) and (<code>x1</code>, <code>y1</code>).
   */
  public boolean intersectsRectangle(float x0, float y0, float x1, float y1) {
    Rectangle2D rectangle = new Rectangle2D.Float(x0, y0, 0, 0);
    rectangle.add(x1, y1);
    return getShape().intersects(rectangle);
  }

  /**
   * Returns <code>true</code> if this room contains 
   * the point at (<code>x</code>, <code>y</code>) with a given <code>margin</code>.
   */
  public boolean containsPoint(float x, float y, float margin) {
    return containsShapeAtWithMargin(getShape(), x, y, margin);
  }

  /**
   * Returns the index of the point of this room equal to 
   * the point at (<code>x</code>, <code>y</code>) with a given <code>margin</code>.
   * @return the index of the first found point or -1.
   */
  public int getPointIndexAt(float x, float y, float margin) {
    for (int i = 0; i < this.points.length; i++) {
      if (Math.abs(x - this.points [i][0]) <= margin && Math.abs(y - this.points [i][1]) <= margin) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Returns <code>true</code> if the center point at which is displayed the name 
   * of this room is equal to the point at (<code>x</code>, <code>y</code>) 
   * with a given <code>margin</code>. 
   */
  public boolean isNameCenterPointAt(float x, float y, float margin) {
    return Math.abs(x - getXCenter() - getNameXOffset()) <= margin 
        && Math.abs(y - getYCenter() - getNameYOffset()) <= margin;
  }

  /**
   * Returns <code>true</code> if the center point at which is displayed the area 
   * of this room is equal to the point at (<code>x</code>, <code>y</code>) 
   * with a given <code>margin</code>. 
   */
  public boolean isAreaCenterPointAt(float x, float y, float margin) {
    return Math.abs(x - getXCenter() - getAreaXOffset()) <= margin 
        && Math.abs(y - getYCenter() - getAreaYOffset()) <= margin;
  }

  /**
   * Returns <code>true</code> if <code>shape</code> contains 
   * the point at (<code>x</code>, <code>y</code>)
   * with a given <code>margin</code>.
   */
  private boolean containsShapeAtWithMargin(Shape shape, float x, float y, float margin) {
    if (margin == 0) {
      return shape.contains(x, y);
    } else {
      return shape.intersects(x - margin, y - margin, 2 * margin, 2 * margin);
    }
  }

  /**
   * Returns the shape matching this room.
   */
  private Shape getShape() {
    if (this.shapeCache == null) {
      GeneralPath roomShape = new GeneralPath();
      roomShape.moveTo(this.points [0][0], this.points [0][1]);
      for (int i = 1; i < this.points.length; i++) {
        roomShape.lineTo(this.points [i][0], this.points [i][1]);
      }
      roomShape.closePath();
      // Cache roomShape
      this.shapeCache = roomShape;
    }
    return this.shapeCache;
  }

  /**
   * Moves this room of (<code>dx</code>, <code>dy</code>) units.
   */
  public void move(float dx, float dy) {
    if (dx != 0 || dy != 0) {
      float [][] points = getPoints();
      for (int i = 0; i < points.length; i++) {
        points [i][0] += dx;
        points [i][1] += dy;
      }
      updatePoints(points);
    }
  }

  /**
   * Returns a clone of this room.
   */
  @Override
  public Room clone() {
    try {
      Room clone = (Room)super.clone();
      clone.propertyChangeSupport = new PropertyChangeSupport(clone);
      clone.level = null;
      return clone;
    } catch (CloneNotSupportedException ex) {
      throw new IllegalStateException("Super class isn't cloneable"); 
    }
  }
}
