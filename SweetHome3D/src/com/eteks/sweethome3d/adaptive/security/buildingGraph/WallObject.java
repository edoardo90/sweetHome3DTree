package com.eteks.sweethome3d.adaptive.security.buildingGraph;


import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingGraphPart;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Shape3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.model.Wall;

public class WallObject extends BuildingGraphPart {

  private Vector3D startPoint;
  private Vector3D endPoint;
  private Shape3D shape;
  private Wall wall;
  
  /**
   * Every point of the shape has to be expressed in cm
   * @param shape
   * @param position
   * @param startPoint
   * @param endPoint
   */
  public WallObject(Shape3D shape, Vector3D startPoint, Vector3D endPoint) {
    
    this.shape = shape;
    this.startPoint = startPoint;
    this.endPoint = endPoint;
    
    float xStart, yStart, xEnd, yEnd, thickness, height;
    
    xStart = (float) this.startPoint.first;
    yStart = (float) this.startPoint.second;
    
    xEnd = (float) this.endPoint.first;
    yEnd = (float) this.endPoint.second;
    thickness = this.getThickness();
    height = this.getHeight();
    
    wall = new Wall(xStart, yStart, xEnd, yEnd, thickness, height);
    
  }
  
  
  public Wall getWall()
  {
    return this.wall;
  }
  
  private float getHeight()
  {
    return 200;
  }
  
  private float getThickness()
  {
    return 50;
  }
  
  @Override
  public String toString()
  {
    return "wall:" + this.shape;
  }
  

}
