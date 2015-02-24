package com.eteks.sweethome3d.junit.decorator;

public class RedShapeDecorator extends ShapeDecorator {
  
  public RedShapeDecorator(Shape decoratedShape) {
    super(decoratedShape);
    
  }

  @Override
  public void draw()
  {
    this.setBorders();
    this.decoratedShape.draw();
  }
  
  private void setBorders()
  {
    System.out.println("border color: red");
  }
  
  
}
