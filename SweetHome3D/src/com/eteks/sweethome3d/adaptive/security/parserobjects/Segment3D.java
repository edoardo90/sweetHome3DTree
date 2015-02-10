package com.eteks.sweethome3d.adaptive.security.parserobjects;

public class Segment3D {
  
  private Vector3D row, tail;
  
  
  public Segment3D(Vector3D a, Vector3D b)
  {
    this.setRow(a);
    this.setTail(b);
  }


  public Vector3D getTail() {
    return tail;
  }

  public void setTail(Vector3D b) {
    this.tail = b;
  }


  public Vector3D getRow() {
    return row;
  }


  public void setRow(Vector3D a) {
    this.row = a;
  }
  
  
  public Vector3D getMidPoint()
  {
    return new Vector3D( (this.row.first + this.tail.first)/2,
                          (this.row.second + this.tail.second)/2,
                          (this.row.third + this.tail.third)/2 );
  }


  public double getLength() {
    return Math.sqrt(
        square(this.row.first - this.tail.first) +
        square(this.row.second - this.tail.second)+
        square(this.row.third - this.tail.third));
  }
  
  
  private double square (double x)  { return x * x; }


  public float getAngle() {
    
    Vector3D originVector = this.row.getSubVector(this.tail);
    return (float) Math.atan2(originVector.second, originVector.first);
    
  }
  
  public double getDistanceFromPoint(Vector3D point)
  {
    return this.pointToLineDistance(this.row, this.tail, point);
  }
  
  private double pointToLineDistance(Vector3D A, Vector3D B, Vector3D point) {
    double normalLength = Math.sqrt((B.first-A.first)*(B.first-A.first)+(B.second-A.second)*(B.second-A.second));
    return Math.abs((point.first-A.first)*(B.second-A.second)-(point.second-A.second)*(B.first-A.first))/normalLength;
  }
  


  @Override
  public String toString() {
    return "Segment3D [row=" + this.row + "\n" +
            ", tail=" + this.tail +  "\n" + 
            ", magnitude=" + this.getLength() + "\n" +
            ", angle="     + this.getAngle() + "]";
  }
  
  
  
  
  
  
  
  
  
}
