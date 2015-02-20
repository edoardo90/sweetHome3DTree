package com.eteks.sweethome3d.adaptive.security.parserobjects;

import java.io.Serializable;



public class Vector3D implements Serializable
{
  public double first;
  public double second;
  public double third;

  public Vector3D(double first, double second, double third)
  {
    if(Math.abs(first)< 0.001)
      first =0;
    if(Math.abs(second)< 0.001)
      second =0;
    if(Math.abs(third)< 0.001)
      third =0;



    this.first = first;
    this.second = second;
    this.third = third;
  }

  public Vector3D getSumVector(Vector3D vector3d) {
    return new Vector3D(this.first + vector3d.first ,
        this.second + vector3d.second,
        this.third  + vector3d.third);
  }

  /**
   * Vector subtraction
   * @param v2
   * @return  this + (- v2 )
   */
  public Vector3D getSubVector(Vector3D v2)
  {
    return this.getSumVector(v2.getNegated());
  }
  
  public Vector3D getVersor()
  {
    double invMagnitude = 1 / this.getMagnitude();
    return this.getScaledVector(invMagnitude);
  }
  
  public Vector3D getNewModule(double newMagnitude)
  {
    Vector3D unit = this.getVersor();
    unit.scale(newMagnitude);
    return unit;
  }
  
  public Vector3D getNegated()
  {
    return new Vector3D(-this.first,-this.second, -this.third); 
  }
  
  public double getMagnitude()
  {
    
    return Math.sqrt( this.first*this.first + this.second * this.second + this.third *this.third);
    
  }
  
  
  
  public Vector3D()
  {
    this(0,0,0);
  }

  public String toStringShortXY()
  {
    return this.toStringShortXY(3);
  }
  
  public String toStringShortXY(int n)
  {
    return "("+ round(this.first,n) + ", " +  round(this.second,n) + ")";
  }

  private double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
  }

  public void sumVector(Vector3D vec)
  {
    this.first += vec.first;
    this.second += vec.second;
    this.third += vec.third;

  }

  @Override
  public String toString()
  {
    return "[ " +  this.first + ", " + this.second + ", " + this.third  + " ]";
  }

  public void scale(float scaleFactor) {
    this.scale((double)scaleFactor);
  }
  
  public void scale(double scaleFactor) {
    this.first *= scaleFactor;
    this.second *= scaleFactor;
    this.third *= scaleFactor;
    
  }
  
  
  public Vector3D getScaledVector(float scaleFactor)
  {
    return new Vector3D(this.first * scaleFactor, this.second * scaleFactor, this.third * scaleFactor);
  }
  
  public Vector3D getScaledVector(double scaleFactor)
  {
    return new Vector3D(this.first * scaleFactor, this.second * scaleFactor, this.third * scaleFactor);
  }
  
  
  
  
  @Override
  public Vector3D clone()
  {
    return new Vector3D(first, second, third);
  }

  private static boolean almostEqual(double a, double b)
  {
    double diff = a - b;
    diff = Math.abs(diff);
    return diff < 10e-05;
  }
  
  
  public boolean almostEqual(Vector3D v2) {
    
    return almostEqual(this.first, v2.first) &&
           almostEqual(this.second, v2.second)  &&
           almostEqual(this.third, v2.third) ;
  }
  
  
  
}
