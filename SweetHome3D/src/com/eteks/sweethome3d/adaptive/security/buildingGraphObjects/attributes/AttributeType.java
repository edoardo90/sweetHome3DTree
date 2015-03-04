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
    for(Method m : methods)
    {
      if(m.getName().equals("valueOf"))
      {
        Object valueOfS = m.invoke(null, s);
        return this.inherentClass.cast(valueOfS);
      }
    }
    return null;
  }
  
}
