package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public enum BuildingObjectType {
  ACTOR, CCTV, LIGHT, PC, PRINTER, HVAC, MAN, WOMAN, UNKNOWN_OBJECT;
  
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
      case UNKNOWN_OBJECT:
         return new UnknownObject(position);
      default:
        break;
    }
    return null;
  }
  
  
  public boolean canStartConnections()
  {
    switch (this)
    {
      case PC:
      case ACTOR:
      case MAN:
      case WOMAN:
        return true;
      default:
        return false;
    }
    
  }
  
 
  public boolean canAcceptConnections()
  {
    switch (this)
    {
      case PC:
      case PRINTER:
        return true;
      default:
        return false;
    }

  }
  
  
  
  
  
}
