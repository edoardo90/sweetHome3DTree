package com.eteks.sweethome3d.adaptive.security.assets.file;

public enum SecurityLevel {
  UNCLASSIFIED("Unclussified"), CONFIDENTIAL("Confidential"), SECRET("Secret"),
  TOP_SECRET("Top secret");

  String text;
  SecurityLevel(String text)
  {
    this.text = text; 
  }

  @Override
  public String toString()
  {
    return this.text;
  }

  protected static SecurityLevel valueOfSmarter(String s)
  {
    try
    {
      return SecurityLevel.valueOf(s);
    }
    catch(Exception e)
    {
      for(SecurityLevel sl : SecurityLevel.values())
      {
        if(s.equals(sl.text))
        {
          return sl;
        }
      }

      return null;
    }
  }


}
