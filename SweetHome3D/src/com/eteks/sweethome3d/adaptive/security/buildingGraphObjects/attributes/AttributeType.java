package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.attributes;

import java.lang.reflect.Method;

public enum AttributeType {
  INT(Integer.class), FLOAT(Float.class), STRING(String.class), BOOLEAN(Boolean.class);
  
  private Class<?> inherentClass;
  
  AttributeType(Class<?> inherentClass)
  {
    this.inherentClass = inherentClass; 
  }
  
  public Object getValueOfString(String s) throws Exception 
  {
    Method[] methods = inherentClass.getMethods();
    Object valueOfS=null;
    for(Method m : methods)
    {
      if(m.getName().equals("valueOf")  )
      {
        int venticinque = Integer.valueOf(s);
        
        try
        {  
           valueOfS = m.invoke(null, s);
        }
        catch(Exception e)
        {
          //it's ok
        }
      }
    }
    Object casted = this.inherentClass.cast(valueOfS); //TODO: do it better ....
    return casted;
    
  }
  
}
