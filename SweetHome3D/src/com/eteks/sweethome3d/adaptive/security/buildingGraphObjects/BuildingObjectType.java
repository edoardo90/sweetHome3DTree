package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public enum BuildingObjectType {
  ACTOR("Actor"), CCTV("CCTV"), LIGHT("Light"), PC("Pc"),
   PRINTER("Printer"), HVAC("HVAC"), MAN("Man"), WOMAN("Woman"), UNKNOWN_OBJECT(""),
   GENERAL_FILE_HOLEDER("FileHolder"), GENERAL_MATERIAL_OBJ("MaterialObject"),
   DOOR("Door");
   
  private String original;
  
  BuildingObjectType(String s)
  {
    original = s;
    
  }
  
  
  /**
   * 
   * @param position
   * @return <pre>  An Appropriate object that stores the type and the position
   *       e.g   ActorObject(position)  ==>   Actor (  (200, 500),  TYPE.ACTOR) )  </pre>
   */
  public  BuildingObjectContained getBuildingObjectOfType(Vector3D position)
  {
    switch(this)
    {
      case ACTOR:
      case MAN:
      case WOMAN:
        return new  ActorObject(position);
      case CCTV:
        return new CCTVObject(position);
      case LIGHT:
        return new LightObject(position);
      case PC:
        return new PCObject(position);
      case PRINTER:
        return new PrinterObject(position);
      case HVAC:
         return new HVACObject(position);
      case GENERAL_MATERIAL_OBJ:
         return new GeneralMaterialObject(position);
      case GENERAL_FILE_HOLEDER:
         return new GeneralFileHolder(position);
      case UNKNOWN_OBJECT:
      case DOOR:
         return new UnknownObject(position);
      default:
        break;
    }
    return null;
  }
  
  public String originalName()
  {
    return this.original;
  }
  
  
  public boolean canConnect()
  {
    switch (this)
    {
      case PC:
      case ACTOR:
      case MAN:
      case PRINTER:
      case WOMAN:
      case CCTV:
        return true;
      default:
        return false;
    }
    
  }
  
  
  
  
}
