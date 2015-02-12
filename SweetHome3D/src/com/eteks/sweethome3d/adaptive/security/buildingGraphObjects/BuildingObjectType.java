package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public enum BuildingObjectType {
  ACTOR, CCTV, LIGHT, PC, PRINTER, HVAC, MAN, WOMAN, UNKNOWN_OBJECT;
  
  public  BuildingObjectContained getBuildingObjectOfType(Vector3D position)
  {
    switch(this)
    {
      case ACTOR:
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
  
}
