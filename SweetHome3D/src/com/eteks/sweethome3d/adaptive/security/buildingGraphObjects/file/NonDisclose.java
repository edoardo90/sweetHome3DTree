package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file;

public enum NonDisclose {
  NDA_PROTECTED("Protected by NDA"), NOT_NDA_PROTECTED("Not protected by NDA");
  
  String s;
  NonDisclose(String s)
  {
    this.s = s;
  }
  @Override
  public String toString()
  {
    return s;
  }
}
