package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.SecurityLevel;

public enum LifeStatus {
   
   BROKEN("Broken"), ON("On"), OFF("Off");
 
   private final String text;
   
   private  LifeStatus(final String text)
   {
     this.text = text;
   }
   
   @Override
   public String toString()
   {
     return this.text;
   }
   
   public static LifeStatus valueOfSmarter(String s)
   {
     try
     {
       return LifeStatus.valueOf(s);
     }
     catch(Exception e)
     {
       if(s.equals("On"))
         return LifeStatus.ON;
       else if(s.equals("Off"))
         return LifeStatus.OFF;
       else
         return LifeStatus.BROKEN;
     }
   }
   
   
   
}
