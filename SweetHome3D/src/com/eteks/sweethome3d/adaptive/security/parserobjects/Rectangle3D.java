package com.eteks.sweethome3d.adaptive.security.parserobjects;

import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.model.Wall;


public class Rectangle3D extends Shape3D
{

  private Vector3D  pointNorthWest, pointSouthEast;
  private boolean isARectangle = true;

  public Vector3D getPointNorthWest()
  {
    return pointNorthWest.clone();
  }

  public Wall getWall()
  {
    List<Segment3D> shorts = this.getShortEdges();
    
    Segment3D sh1 = shorts.get(0);
    Segment3D sh2 = shorts.get(1);
    
    Vector3D mid1 = sh1.getMidPoint();
    Vector3D mid2 = sh2.getMidPoint();
    
    float xStart = (float)mid1.first;
    float yStart = (float)mid1.second;
    float xEnd = (float)mid2.first;
    float yEnd = (float)mid2.second;
    
    
    
    return new Wall(xStart, yStart, xEnd, yEnd, 50, 200);
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
  
  public float getAngleOfLongsEdges()
  {
    
   Segment3D longEdge =  this.getLongEdges().get(0);
    return longEdge.getAngle();
    
  }
  
  public Vector3D perpendicularVectorTowardsInside(Vector3D startingPoint)
  {
    List<Segment3D>  longs = getLongEdges();
    Segment3D long1 = longs.get(0);
    Segment3D long2 = longs.get(1);
    
    double d1 = long1.getDistanceFromPoint(startingPoint);
    double d2 = long2.getDistanceFromPoint(startingPoint);
    
    double maxDistanceLong = d1 > d2 ? d1 : d2;
    double minDistanceLong = d1 < d2 ? d1 : d2;
    
    Segment3D farLong, closeLong;
    
    if(d1 > d2)
    {
      farLong = long1;
      closeLong = long2;
    }
    else
    {
      farLong = long2;
      closeLong = long1;
    }
    
    Vector3D directedBaseVector = farLong.getTail().getSubVector(closeLong.getTail());
    
    double deltaX = startingPoint.first - directedBaseVector.first;
    double deltaY =  startingPoint.second - directedBaseVector.second ;
    
    directedBaseVector.first += deltaX;
    directedBaseVector.second += deltaY;
    
    Vector3D perpVect = directedBaseVector.getNewModule(this.getMinEdge() + minDistanceLong + 30 );
    
    return perpVect;
  }
  

  public List<Segment3D> getLongEdges()
  {
    
    Segment3D nn1 = new Segment3D(this.getPointNorthWest(), this.getPointNorthEast());
    Segment3D ns1 = new Segment3D(this.getPointNorthEast(), this.getPointSouthEast());
    Segment3D ns2 = new Segment3D(this.getPointNorthWest(), this.getPointSouthWest() );
    Segment3D nn2 = new Segment3D(this.getPointSouthWest(), this.getPointSouthEast());
    
    
    double w = nn1.getLength();
    double h = ns1.getLength();
    
    List<Segment3D> lst = new ArrayList<Segment3D>();
    if(w < h)
    {
      lst.add(ns1);
      lst.add(ns2);
    }
    else
    {
      lst.add(nn1);
      lst.add(nn2);
    }
    
    return lst;
    
  }
  
  
  
  public List<Segment3D> getShortEdges()
  {
    
    Segment3D nn1 = new Segment3D(this.getPointNorthWest(), this.getPointNorthEast());
    Segment3D ns1 = new Segment3D(this.getPointNorthEast(), this.getPointSouthEast());
    Segment3D ns2 = new Segment3D(this.getPointNorthWest(), this.getPointSouthWest() );
    Segment3D nn2 = new Segment3D(this.getPointSouthWest(), this.getPointSouthEast());
    
    double w = nn1.getLength();
    double h = ns1.getLength();
    
    List<Segment3D> lst = new ArrayList<Segment3D>();
    if(w > h)
    {
      lst.add(ns1);
      lst.add(ns2);
    }
    else
    {
      lst.add(nn1);
      lst.add(nn2);
    }
    
    return lst;
    
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

  
  private double sqr(double x)
  {
    return Math.sqrt(Math.abs(x));
  }
  private boolean  isRectangle(double x1, double y1,
                               double x2, double y2,
                               double x3, double y3,
                               double x4, double y4)
  {
    double cx,cy;
    double dd1,dd2,dd3,dd4 ;

    cx=(x1+x2+x3+x4)/4;
    cy=(y1+y2+y3+y4)/4;

    dd1=sqr(cx-x1)+sqr(cy-y1);
    dd2=sqr(cx-x2)+sqr(cy-y2);
    dd3=sqr(cx-x3)+sqr(cy-y3);
    dd4=sqr(cx-x4)+sqr(cy-y4);
    return ( Math.abs(dd1-dd2) < 10E-06) && (Math.abs(dd1-dd3)<10E-06)
            && (Math.abs(dd1-dd4)<10e-06);
  }
  
  public boolean isARectangle()
  {
    List<Vector3D> list = this.getListOfPoints();
    
    if(!this.isARectangle)
      return false;
    
    double x1; double y1;
    double x2; double y2;
    double x3; double y3;
    double x4; double y4;
    
    x1 = list.get(0).first;
    y1 = list.get(0).second;
    
    x2 = list.get(1).first;
    y2 = list.get(1).second;
    
    x3 = list.get(2).first;
    y3 = list.get(2).second;
    
    x4 = list.get(3).first;
    y4 = list.get(3).second;
    
    return isRectangle(x1, y1, x2, y2, x3, y3, x4, y4);

  }
  

  public Rectangle3D(List<Vector3D> list)
  {
    if(list == null || list.size() != 4)
    {
      this.pointNorthWest = new Vector3D();
      this.pointSouthEast = new Vector3D(1,1,0);
      this.isARectangle = false;
      throw new IllegalStateException("not a rectangle!");

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

       if(! this.isARectangle())
       {
         this.isARectangle = false;
         throw new IllegalStateException("not a rectangle!");
         
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
