package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

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
   
   
}
