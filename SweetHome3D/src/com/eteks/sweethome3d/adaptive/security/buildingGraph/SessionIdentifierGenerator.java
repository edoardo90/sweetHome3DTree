package com.eteks.sweethome3d.adaptive.security.buildingGraph;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class SessionIdentifierGenerator {
  private SecureRandom random = new SecureRandom();
  private static SessionIdentifierGenerator instance;
  
  public static SessionIdentifierGenerator getInstance()
  {
    if(instance == null)
    {
      instance = new SessionIdentifierGenerator();
    }
    return instance;
  }
  
  private SessionIdentifierGenerator()
  { 
  }
  
  public String nextSessionId() {
    return new BigInteger(130, random).toString(32);
  }
}
