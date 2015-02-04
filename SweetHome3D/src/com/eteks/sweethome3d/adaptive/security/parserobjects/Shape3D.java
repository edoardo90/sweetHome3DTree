package com.eteks.sweethome3d.adaptive.security.parserobjects;
import java.util.List;

import com.eteks.sweethome3d.model.Room;


public abstract class Shape3D {
  
  public abstract List<Vector3D> getListOfPoints();
  
  
  public Room getRoom() {
    
    List<Vector3D> points = this.getListOfPoints();
    
    float x1,x2,y1,y2;
    x1=(float)points.get(0).first;
    y1=(float)points.get(0).second;
    x2=(float)points.get(1).first;
    y2=(float)points.get(1).second;
    
    float [][] pointsF = new float [][]{{x1, y1} , {x2, y2}};
    
    Room r = new Room(pointsF);
    for(int i = 2; i<points.size(); i++)
    {
      Vector3D vect = points.get(i);
      
      r.addPoint((float) ( vect.first), (float)( vect.second));
    }

    return r;
  }
  
  @Override
  public String toString()
  {
    if(this instanceof Rectangle3D)
    {
      Rectangle3D r3d = (Rectangle3D)this;
      return "" + r3d;
    }
    else
    {
      return "" + getListOfPoints();
    }
  }

  public abstract void scale(float scaleFactor);
  
}
