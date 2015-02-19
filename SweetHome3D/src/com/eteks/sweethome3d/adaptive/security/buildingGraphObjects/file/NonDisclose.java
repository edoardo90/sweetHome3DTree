package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file;

public enum NonDisclose {
  NDA_PROTECTED("Protected by NDA"), NOT_NDA_PROTECTED("Not protected by NDA");
  
  String niceString;
  NonDisclose(String s)
  {
    this.niceString = s;
  }
  @Override
  public String toString()
  {
    return niceString;
  }
  
  
  protected static NonDisclose valueOfSmarter(String s)
  {
    try
    {
      return NonDisclose.valueOf(s);
    }
    catch(Exception e)
    {
      for(NonDisclose nd :  NonDisclose.values())
      {
      if(s.equals(nd.niceString))
      {
        return nd;
      }
     }
       
        return null;
    }
  }
  
}
