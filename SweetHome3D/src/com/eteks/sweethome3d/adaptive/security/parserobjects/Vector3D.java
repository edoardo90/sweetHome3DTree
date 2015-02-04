package com.eteks.sweethome3d.adaptive.security.parserobjects;



public class Vector3D
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

  public Vector3D()
  {
    this(0,0,0);
  }

  public String toStringShortXY()
  {
    return "("+ round(this.first,3) + ", " +  round(this.second,3) + ")";
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
}
