package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.attributes;

import java.io.Serializable;

import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * name || type  ||  value
 * @author Edoardo Pasi
 */
public class BuildingObjectAttribute {
  private  String name;
  private  AttributeType type;
  private  Object value;
  
  public BuildingObjectAttribute(String name, AttributeType type, Object value)
  {
    this.name = name;
    this.type = type;
    this.checkTypeAgainstValue(type, value);
    this.value = value;
    
  }

  public BuildingObjectAttribute(String fileRowAttribute) {
    String [] cols = fileRowAttribute.split(",");

    String attribName    = cols[1];
    String attribType    = cols[2];
    String defaultValue  = cols[3];
    
    AttributeType type = null;
    try
    {
      type = AttributeType.valueOf(attribType);
    }
    catch(Exception e )
    {
      e.printStackTrace();
      throw new IllegalStateException(" the type : " + attribType + " is not valid ");
    }
    Object value = null;
    try
    {
      value = type.getValueOfString(defaultValue);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      throw new IllegalStateException("the value:" + defaultValue +
          " is not convertible into the type: " + attribType);
    }
    this.value = value;
    this.name = attribName;
    this.type = type;
    
  }

  private void checkTypeAgainstValue(AttributeType type, Object value) {
    Class<?> initialValueClazz = value.getClass();
    switch (type) {
      case BOOLEAN:
           if(! initialValueClazz.equals(Boolean.class))
           {
             throw new IllegalArgumentException("if type is BOOLEAN then value class should be Booloean");
           }
           break;
      case FLOAT:
        if(! initialValueClazz.equals(Float.class))
        {
          throw new IllegalArgumentException("if type is FLOAT then value class should be Float");
        }
           break;
      case INT:
        if(! initialValueClazz.equals(Integer.class))
        {
          throw new IllegalArgumentException("if type is INT then value class should be Integer");
        }
           break;
      case STRING:
        if(! initialValueClazz.equals(String.class))
        {
          throw new IllegalArgumentException("if type is STRING then value class should be String");
        }
           break;
      default:
        break;
    }

    
  }

  public void setNameTypeValue(BuildingObjectAttribute attr)
  {
    String name = attr.getName();
    AttributeType type = attr.getType();
    Object value = attr.getValue();
    checkTypeAgainstValue(type, value);
    this.name = name;
    this.type = type;
    this.value = value;
  }
  
  public String toString()
  {
    return this.getName() + "," + this.getType().name() + ",\n\tValue:" + this.getValue();
  }
  
  public String getName() {
    return name;
  }

  public AttributeType getType() {
    return type;
  }

  public Object getValue() {
    return value;
  }
  
}
