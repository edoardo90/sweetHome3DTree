package com.eteks.sweethome3d.adaptive.security.parserobjects;

import Jama.Matrix;

public class Axis3DNice
{

  public Vector3D xVector = this.getDefaultVector(1),
      yVector = this.getDefaultVector(2),
      zVector = this.getDefaultVector(3);

  @Override
  public String toString()
  {
    return "X: " + this.xVector + "\n" +
        "Y: " + this.yVector + "\n" +
        "Z: " + this.zVector ;
  }

  public Axis3DNice()
  {
    this(1,0,0,   0,1,0,   0,0,1);
  }

  public Axis3DNice(double x1, double x2,double x3,double y1,double y2,double y3,double z1,double z2,double z3)
  {
    this.xVector.first = x1;
    this.xVector.second = x2;
    this.xVector.third = x3;

    this.yVector.first = y1;
    this.yVector.second = y2;
    this.yVector.third = y3;


    this.zVector.first = z1;
    this.zVector.second = z2;
    this.zVector.third = z3;
  }

  /** 
   * Converts coordinates of point express in the basis of this Axis3D into canonical coordinates
   * 
   * Let V a vector in a space in the form [v1, v2, v3],   and let A [Ax, Ay, Az] a basis of that space
   * then V expressed in canonical form is obtained as V_canonical = A * V
   * 
   * @return the coordinate respect to the canonical  standard word (xyz)=(1,0,0  0,1,0   0,0,1)
   */
  public Vector3D getDefaultWordCoordinate(Vector3D coordinate)
  {
    double v1,v2, v3;
    v1 = coordinate.first;
    v2 = coordinate.second;
    v3 = coordinate.third;


    double[][] array = this.getMatrix();
    Matrix A = new Matrix(array);   //basis matrix of this 3d axes
    Matrix v = new Matrix(new double[]{v1, v2, v3}, 1); //row vector     

    Matrix vCanonical = A.times(v.transpose());

    double first = vCanonical.get(0, 0);
    double second = vCanonical.get(1, 0);
    double third = vCanonical.get(2, 0);






    Vector3D vCanonicalNice = new Vector3D(first, second, third);

    return vCanonicalNice;
  }

  private double[][] getMatrix()
  {
    double x1,x2,x3,y1,y2,y3,z1,z2,z3;

    x1 = this.xVector.first;
    x2 = this.xVector.second;
    x3 = this.xVector.third;

    y1 = this.yVector.first;
    y2 = this.yVector.second;
    y3 = this.yVector.third;


    z1 = this.zVector.first;
    z2 = this.zVector.second;
    z3 = this.zVector.third;


    double [][] array = {{x1,y1,z1}, {x2,y2,z2}, {x3,y3,z3}};

    return array;
  }

  private double scalarProduct(Vector3D v1, Vector3D v2)
  {
    return v1.first * v2.first + v1.second * v2.second + v1.third * v2.third;
  }

  private Vector3D getDefaultVector(int i)
  {
    Vector3D vec = new Vector3D(0, 0, 0);
    switch (i) {
      case 1:
        vec.first = 1;
        break;
      case 2:
        vec.second = 1;
        break;
      case 3:
        vec.third = 1;
        break;
      default:
        return null;

    }
    return vec;

  }
}
