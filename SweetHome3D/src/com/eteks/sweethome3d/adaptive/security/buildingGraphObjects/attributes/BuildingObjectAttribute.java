package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.attributes;

public class BuildingObjectAttribute {
  private final String name;
  private final AttributeType type;
  private final Object initialValue;
  
  public BuildingObjectAttribute(String name, AttributeType type, Object initialValue)
  {
    this.name = name;
    this.type = type;
    Class<?> initialValueClazz = initialValue.getClass();
    switch (type) {
      case BOOLEAN:
           if(! initialValueClazz.equals(Boolean.class))
           {
             throw new IllegalArgumentException("if type is BOOLEAN then initialValue class should be Booloean");
           }
           break;
      case FLOAT:
        if(! initialValueClazz.equals(Float.class))
        {
          throw new IllegalArgumentException("if type is FLOAT then initialValue class should be Float");
        }
           break;
      case INT:
        if(! initialValueClazz.equals(Integer.class))
        {
          throw new IllegalArgumentException("if type is INT then initialValue class should be Integer");
        }
           break;
      case STRING:
        if(! initialValueClazz.equals(String.class))
        {
          throw new IllegalArgumentException("if type is STRING then initialValue class should be String");
        }
           break;
      default:
        break;
    }
    
    this.initialValue = initialValue;
    
  }

  public String getName() {
    return name;
  }

  public AttributeType getType() {
    return type;
  }

  public Object getInitialValue() {
    return initialValue;
  }
  
}
