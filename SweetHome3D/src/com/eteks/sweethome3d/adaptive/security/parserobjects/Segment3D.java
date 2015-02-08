package com.eteks.sweethome3d.adaptive.security.parserobjects;

public class Segment3D {
  
  private Vector3D a, b;
  
  
  public Segment3D(Vector3D a, Vector3D b)
  {
    this.setA(a);
    this.setB(b);
  }


  public Vector3D getB() {
    return b;
  }

  public void setB(Vector3D b) {
    this.b = b;
  }


  public Vector3D getA() {
    return a;
  }


  public void setA(Vector3D a) {
    this.a = a;
  }
  
  
  public Vector3D getMidPoint()
  {
    return new Vector3D( (this.a.first + this.b.first)/2,
                          (this.a.second + this.b.second)/2,
                          (this.a.third + this.b.third)/2 );
  }


  public double getLength() {
    return Math.sqrt(
        square(this.a.first - this.b.first) +
        square(this.a.second - this.b.second)+
        square(this.a.third - this.b.third));
  }
  
  
  private double square (double x)  { return x * x; }


  public float getAngle() {
    
    Vector3D originVector = this.a.getSubVector(this.b);
    return (float) Math.atan2(originVector.second, originVector.first);
    
  }


  @Override
  public String toString() {
    return "Segment3D [a=" + this.a + "\n" +
            ", b=" + this.b +  "\n" + 
            ", magnitude=" + this.getLength() + "\n" +
            ", angle="     + this.getAngle() + "]";
  }
  
  
  
  
  
  
  
  
  
}
