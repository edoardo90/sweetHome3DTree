package com.eteks.sweethome3d.adaptive.security.parserobjects;

import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.model.Wall;


public class Rectangle3D extends Shape3D
{

  private Vector3D  pointNorthWest, pointSouthEast;

  public Vector3D getPointNorthWest()
  {
    return pointNorthWest.clone();
  }
  
  public Wall getWall()
  {
    
    Vector3D NN = pointNorthWest.getSubVector(getPointNorthEast());
    Vector3D SN = pointSouthEast.getSumVector(getPointSouthWest());
    
    Vector3D longest = NN.getMagnitude() > SN.getMagnitude() ?  NN  : SN;
    
    float xStart, yStart, yEnd, xEnd;
    if(longest == NN)
    {
      xStart = this.midXWest();
      yStart = this.midYWest();
      
      xEnd =  this.midXEast();
      yEnd   = this.midYEast();
      
    }
    else
    {
      xStart = this.midXNorth();
      yStart = this.midYNorth();
      
      xEnd = this.midXSouth();
      yEnd = this.midYSouth();
      
    }
    
    return new Wall(xStart, yStart, xEnd, yEnd, 40, 200);
  }
  
  private float midY(Vector3D v1, Vector3D v2)
  {
    return (float) ( v1.second + v2.second) / 2;
  }
  private float midX(Vector3D v1, Vector3D v2)
  {
    return (float) ( v1.first + v2.first) / 2;
  }
  
  
    
  
  

  private float midYNorth() {
    return this.midY(this.getPointNorthEast(), this.getPointNorthWest());
  }

  private float midYSouth() {
     return midY(this.getPointSouthEast(), this.getPointSouthWest());
  }

  private float midXSouth() {
    return midX(this.getPointSouthEast(), this.getPointSouthWest());
  }


  private float midXNorth() {
    return this.midX(this.getPointNorthEast(), this.getPointNorthWest());
  }

  private float midYEast() {
     return this.midY(this.getPointSouthEast(), this.getPointNorthEast());
  }

  private float midXEast() {
   
    return this.midX(this.getPointNorthEast(), this.getPointSouthEast());
  }

  private float midYWest() {
   return this.midY(this.getPointSouthWest(), this.getPointNorthWest());
  }

  private float midXWest() {
    return this.midX(this.getPointSouthWest(), this.getPointNorthWest());
  }

  
  
  
  public double getArea() 
  {
    double xDim = Math.abs( this.pointNorthWest.first - this.pointSouthEast.first );
    double yDim = Math.abs( this.pointNorthWest.second - this.pointSouthEast.second);
    return xDim * yDim;
  }

  public double getHeight()
  {
    return  Math.abs( this.pointNorthWest.second - this.pointSouthEast.second);
  }

  public double getWidth() 
  {
    return Math.abs( this.pointNorthWest.first - this.pointSouthEast.first ); 
  }

  public double getMinEdge() { return min(getHeight(), getWidth()); }

  private double min(double x, double y)  {     return x<y? x : y;   }

  public Vector3D getPointSouthEast()
  {
    return this.pointSouthEast.clone();
  }

  @Override
  public String toString()
  {
    String p1 = this.getPointNorthWest().toStringShortXY();
    String p2 = this.getPointNorthEast().toStringShortXY();
    String p3 = this.getPointSouthWest().toStringShortXY();
    String p4 = this.getPointSouthEast().toStringShortXY();

    return  p1 + " ----------------------- " + p2  +
        "\n | " +  spacesFor(p1 + " ----------------------- " + p2  ) + "| " +  
        "\n | " +  spacesFor(p1 + " ----------------------- " + p2  ) + "| " +
        "\n | " +  spacesFor(p1 + " ----------------------- " + p2  ) + "| " +
        "\n +"  + p3 +  "--------------"  + p4 + "\n"; 
  }


  /* 
   *  <---x_DIM---------->   
   *  1------------------2    ^
   *  |                  |    |            ^
   *  |         O        |  Y_DIM          |
   *  |                  |    |            y
   *  4------------------3    v            |---x-->
   */
  public Vector3D getPointNorthEast()
  {

    double x1, y1, x2, x3, y2, y3, x4, y4;

    x1 = this.pointNorthWest.first;
    y1 = this.pointNorthWest.second;

    x3 = this.pointSouthEast.first;
    y3 = this.pointSouthEast.second;

    x2 = x3;
    y2 = y1;

    x4= x1;
    y4 = y3;

    return new Vector3D(x2, y2, this.pointNorthWest.third);

  }

  public Vector3D getPointSouthWest()
  {

    double x1, y1, x2, x3, y2, y3, x4, y4;

    x1 = this.pointNorthWest.first;
    y1 = this.pointNorthWest.second;

    x3 = this.pointSouthEast.first;
    y3 = this.pointSouthEast.second;

    x2 = x3;
    y2 = y1;

    x4= x1;
    y4 = y3;

    return new Vector3D(x4, y4, this.pointNorthWest.third);

  }

  @Override
  public List<Vector3D> getListOfPoints()
  {
    //get copies of vectors: new 3d vectors
    Vector3D p1 = this.getPointNorthEast();
    Vector3D p2 = this.getPointNorthWest();
    Vector3D p3 = this.getPointSouthWest();
    Vector3D p4 = this.getPointSouthEast();
    List<Vector3D> lst = new ArrayList<Vector3D>();
    
    lst.add(p1);
    lst.add(p2);
    lst.add(p3);
    lst.add(p4);

    return lst;
  }


  /* 
   *  <---x_DIM---------->   
   *  1------------------2    ^
   *  |                  |    |            ^
   *  |         O        |  Y_DIM          |
   *  |                  |    |            y
   *  4------------------3    v            |---x-->
   */

  public Rectangle3D(Vector3D center, double xDim, double yDim)
  {
    double x1, y1, z1 = center.third,   x3,y3,z3 = center.third;

    x1 = center.first - xDim / 2;
    y1 = center.second + yDim / 2;
    x3 = center.first + xDim / 2;
    y3 = center.second - yDim / 2;

    this.pointNorthWest = new Vector3D(x1, y1, z1);
    this.pointSouthEast = new Vector3D(x3, y3, z3);

  }


  public Rectangle3D(List<Vector3D> list)
  {
    if(list == null || list.size() != 4)
    {
      this.pointNorthWest = new Vector3D();
      this.pointSouthEast = new Vector3D(1,1,0);
    }
    else
    {

      /*
       * NW    N    NE
       *    
       * W     +     E
       *     
       *SW     S    SE
       * 
       */

      double minX = 0, maxY=0;
      minX = list.get(0).first;
      maxY = list.get(0).second;
      for(int i=0;i<4;i++)
      { 
        double x = list.get(i).first;
        double y = list.get(i).second;
        if(x <= minX)
          minX = x;
        if(y >= maxY)
          maxY = y;
      }

      for(int i=0;i<4;i++)
      {
        double x = list.get(i).first;
        double y = list.get(i).second;

        if(x==minX && y == maxY)
        {
          this.pointNorthWest = list.get(i);
        }
        if(x!=minX && y!= maxY)
        {
          this.pointSouthEast = list.get(i);
        }

      }

    }
  }

  
  private String spacesFor(String s)
  {
    String acc = "";
    for(int i=0; i<s.length()-2; i++)
    {
      acc = acc + " ";
    }
    return acc;
  }


  @Override
  public void scale(float scaleFactor) {
    this.pointNorthWest.scale(scaleFactor);
    this.pointSouthEast.scale(scaleFactor);
    
  }




}
