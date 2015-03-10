package com.eteks.sweethome3d.adaptive.security.parserobjects;

import java.awt.Polygon;
import java.awt.geom.Area;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.eteks.sweethome3d.model.RoomGeoSmart;
import com.eteks.sweethome3d.model.Wall;

/**
 * <pre>
  * 
  * Intances of this class are supposed to be rectangles
  * without rotations applied, so with each edge parallel to X axis or 
  * to Y axis
  *
  * NW                     NE 
  *    <---x_DIM---------->   
  *    1------------------2    ^
  *    |                  |    |            ^
  *    |         O        |  Y_DIM          |
  *    |                  |    |            y
  *    4------------------3    v            |---x-->
  * SW                     SE
 * 
 * 
 * NE: North East
 * NW: North West
 * SW: South West
 * SE: South East
 * 
 * </pre>
 * 
 * @author Edoardo Pasi
 */

public class Rectangle3D extends Shape3D implements Serializable
{

  private Vector3D  pointNorthWest, pointSouthEast;
  
  private Vector3D  pointNorthEast = null, pointSouthWest = null;
  
  private String rectName;
  
  private boolean isARectangle = true;

  /***
   * 
   * <pre> 
   *  <---x_DIM---------->   
   *  1------------------2    ^
   *  |                  |    |            ^
   *  |         O        |  Y_DIM          |
   *  |                  |    |            y
   *  4------------------3    v            |---x-->
   *  
   *  </pre>
   * 
   * @return the North East point
   */
  public Vector3D getPointNorthEast()
  {
      return this.pointNorthEast;
  }

  public Vector3D getPointNorthWest()
  {
    return pointNorthWest.clone();
  }

  public Vector3D getPointSouthWest()
  {
        return this.pointSouthWest;
  }

  public Vector3D getPointSouthEast()
  {
    return this.pointSouthEast.clone();
  }
  /**
   * This method gets the long edges of the rectangle 
   * and returns a wall built between them
   * @return
   */
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
    
    
    
    return new Wall(xStart, yStart, xEnd, yEnd, 20, 200);
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

  /**
   * In future implementation the Rectangle 3D may be also rotated
   * @return
   */
  public float getAngleOfLongsEdges()
  {
    
   Segment3D longEdge =  this.getLongEdges().get(0);
    return longEdge.getAngle();
    
  }
  /**
   * <pre>
   *           ----------
   *           |         |
   *           |         |
   *    .P     |  RECT   |
   *           |         |
   *           |         |
   *           |         |
   *           -----------
   *           
   *           ----------
   *           |         |
   *           |         |
   *   P.|--V------>     |
   *           |         |
   *           |         |
   *           |         |
   *           -----------
   *                      
   * </pre>
   * @param startingPoint  P
   * @return V
   */
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
    
    double deltaX = startingPoint.first ;
    double deltaY =  startingPoint.second ;
    
    double minEdge = this.getMinEdge();
    
    Vector3D directedLonger = directedBaseVector.getNewModule(minEdge + minDistanceLong + 30 );
    
    directedLonger.first += deltaX;
    directedLonger.second += deltaY;        
    
    Vector3D perpVect = directedLonger;
    
    return directedLonger;
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
  
  /***
   * 
   * <pre>
   * Counterclock
   * It relies on getPointNorthEast() and so on
   * lst[0] = NE
   * lst[1] = NW
   * lst[2] = SW
   * lst[3] = SE
   * 
   * </pre>
   */
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
  
  /* 
   *  <---x_DIM---------->   
   *  1------------------2    ^
   *  |                  |    |            ^
   *  |      Center      |  Y_DIM          |
   *  |                  |    |            y
   *  4------------------3    v            |---x-->
   */
  /**
   * The rectangle created in this way have edges parallel to X or Y axis
   * @param center
   * @param xDim
   * @param yDim
   */
 
  public Rectangle3D(Vector3D center, double xDim, double yDim)
  {
    double x1, y1, z1 = center.third,   x3,y3,z3 = center.third;
    
    
    x1 = center.first - xDim / 2;
    y1 = center.second + yDim / 2;
    x3 = center.first + xDim / 2;
    y3 = center.second - yDim / 2;
  
    this.pointNorthWest = new Vector3D(x1, y1, z1);
    this.pointNorthEast = new Vector3D(x3, y1, 0);
    
    this.pointSouthEast = new Vector3D(x3, y3, z3);
    this.pointSouthWest = new Vector3D(x1, y3, 0);
  
  }

  /**
   * 
   * @param nE
   * @param nW
   * @param sW
   * @param sE
   */

  public Rectangle3D(Vector3D nE, Vector3D nW, Vector3D sW, Vector3D sE)
  {
    /***
     *  NW        NE  
     *    1     2
     *    
     *    3     4
     *  SW        SE
     * 
     */
      constructorCompassPoints(nE, nW, sW, sE);
   }
  
  public Rectangle3D(List<Vector3D> list)
  {
      if(list != null && list.size() == 4)
      {
        try{
        this.constructorCompassPoints(list.get(0),
                                      list.get(1),
                                      list.get(2),
                                      list.get(3));
        }
        catch(IllegalStateException e)
        {
          e.printStackTrace();
        }
      }
      else
      {
        throw new IllegalStateException("Rectangle 3D list contructor"
            + " needs a list with 4 vecto3d inside ");
      }
  }

  private void constructorCompassPoints(Vector3D nE, Vector3D nW, Vector3D sW, Vector3D sE)
  {
    Map<Compass, Vector3D> map = this.getMapFromPoints(nW, nE, sE, sW);
    
    this.pointNorthWest = map.get(Compass.NW);
    this.pointSouthEast = map.get(Compass.SE);
    this.pointNorthEast = map.get(Compass.NE);
    this.pointSouthWest = map.get(Compass.SW);
  }

  private void constructListOldWay(List<Vector3D> list)
  {
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
  
  private Map<Compass, Vector3D> getMapFromPoints(Vector3D nW, Vector3D nE, Vector3D sE, Vector3D sW)
  {
    TreeSet<Vector3D> ts = new TreeSet<Vector3D>(new Comparator<Vector3D>() {
  
      public int compare(Vector3D v1, Vector3D v2) {
        
          if(v1.second < v2.second)
            return 1;
          if(v1.second > v2.second)
            return -1;
  
          if(v1.first  < v2.first)
            return -1;
          if(v1.first  > v2.first)
            return 1;
  
          return 0;
        
      }
    });
    
    ts.add(nW);
    ts.add(nE);
    ts.add(sE);
    ts.add(sW);
    
    List<Vector3D> points = new ArrayList<Vector3D>(ts);
    
    Vector3D nW1 = points.get(0);
    Vector3D nE1 = points.get(1);
    Vector3D sW1 = points.get(2);
    Vector3D sE1 = points.get(3);
    
    Segment3D NN = new Segment3D(nE1, nW1);
    Segment3D WW = new Segment3D(nW1, sW1);
    boolean perp = NN.isPerpendicular(WW);
    Segment3D SS = new Segment3D(sW1, sE1);
    Segment3D EE = new Segment3D(nE1, sE1);
  
    perp = perp && SS.isPerpendicular(EE);
    this.isARectangle = perp;
    
    if(! this.isARectangle)
    {
      throw new IllegalStateException("points does not form a rect!!");
    }
    
    Map<Compass, Vector3D> compm  = new HashMap<Compass, Vector3D>();
    compm.put(Compass.NE, nE1);
    compm.put(Compass.NW, nW1);
    compm.put(Compass.SE, sE1);
    compm.put(Compass.SW, sW1);
    return compm;
  }


  public enum Compass
  {
    NE, NW, SE, SW;
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
    this.pointNorthEast.scale(scaleFactor);
    this.pointSouthWest.scale(scaleFactor);
  }

  public Segment3D getTopSegment() {
    Vector3D pn1 = this.getPointNorthEast();
    Vector3D pn2 = this.getPointNorthWest();
    Segment3D topSegm = new Segment3D(pn1, pn2);
    return topSegm;
  }

  public boolean contains(Vector3D position) {
    
    RoomGeoSmart rg = new RoomGeoSmart(this);
    return rg.containsPoint(position);
    
  }

  public boolean contains(Rectangle3D rect) {
    RoomGeoSmart rg = new RoomGeoSmart(this);
    RoomGeoSmart rg2 = new RoomGeoSmart(rect);
    Polygon p1 = rg.getPolygon1000xBigger();
    Polygon p2 = rg2.getPolygon1000xBigger();
    Area a1 = new Area(p1);
    Area a2 = new Area(p2);
    Area a1copy = (Area) a1.clone();
    a1.intersect(a2);
    boolean eq = a1.equals(a2);
    return eq;
  }

  public String getRectName() {
    return rectName;
  }

  public void setRectName(String rectName) {
    this.rectName = rectName;
  }




}
