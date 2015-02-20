package com.eteks.sweethome3d.junit.adaptive.geometry;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Segment3D;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.junit.adaptive.BasicTest;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.UserPreferences;

public class SegmentTest extends BasicTest {

  public void testmagnitudeTest()
  {
    
    Vector3D p1 = new Vector3D(3, 1, 0);
    Vector3D p2 = new Vector3D(2, 4, 0);
    
    Segment3D s = new Segment3D(p1, p2);
    
    double rad10 = Math.sqrt(10);
    
    double magnitued = s.getLength();
    
    double diff = Math.abs( magnitued - rad10);
    
    boolean diffmin = diff < 10e-06;
    
    System.out.println(p1.getNegated());
    
    Vector3D p2MenP1 = p2.getSubVector(p1);
    
    System.out.println(p2MenP1.getMagnitude());
    System.out.println(rad10);
    
    System.out.println(p2MenP1);
    
    assertTrue("wrong magnitude", diffmin);
    
  }
  
  
  @Override
  public void doStuffInsideMain(Home home, UserPreferences preferences) {
   
    
  }

}
