package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file;

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
  
}
