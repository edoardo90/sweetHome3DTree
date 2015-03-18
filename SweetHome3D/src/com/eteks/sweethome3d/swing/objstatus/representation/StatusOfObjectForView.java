package com.eteks.sweethome3d.swing.objstatus.representation;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.eteks.sweethome3d.adaptive.security.assets.ObjectAbility;
import com.eteks.sweethome3d.adaptive.security.assets.attributes.BuildingObjectAttribute;

public  class StatusOfObjectForView
{
  private final String objName;
  private final String lifeStatus;
  private final List<String> files;
  private final List<String> objContainedInside;
  private final List<BuildingObjectAttribute> attributes;
  private Set<ObjectAbility> abilities;

  public StatusOfObjectForView(  final String objName, final List<String> objContainedInside,
                                 final List<BuildingObjectAttribute> attributes,
                                 final String lifeStatus, final List<String> files)
  {
    this.objName = objName;
    this.lifeStatus =  lifeStatus;
    this.attributes = attributes;
    this.objContainedInside = objContainedInside;
    if(files == null)
    {
      this.files = null;
    }
    else
    {
      this.files = Collections.unmodifiableList(files);
    }
  }

  public StatusOfObjectForView(final String lifeStatus, final List<String> files)
  {
    this("", null,null, lifeStatus, files);
  }
  
  public StatusOfObjectForView(final String objName,  final String lifeStatus, final List<String> files)
  {
    this(objName, null,null, lifeStatus, files);
  }
   

  public StatusOfObjectForView( final String lifeStatus, final List<String> files, final List<String> objectsContained)
  {
    this("", objectsContained, null, lifeStatus, files);
  }

  
  public StatusOfObjectForView(StatusOfObjectForView basic, List<String> files, String FILES)
  {
    this(basic.getName(), basic.getObjectContainedLst(), basic.getObjectAttributes(), basic.getLifeStatus(), files);
  }
  
  public StatusOfObjectForView(StatusOfObjectForView basic, List<String> contained, Boolean CONTAINED)
  {
    this(basic.getName(), contained, basic.getObjectAttributes(), basic.getLifeStatus(), basic.getFiles());
  }
  
  public StatusOfObjectForView(StatusOfObjectForView basic, List<BuildingObjectAttribute> attrs, Integer ATTRS)
  {
    this(basic.getName(), basic.getObjectContainedLst(), attrs, basic.getLifeStatus(), basic.getFiles());
  }

  public StatusOfObjectForView(StatusOfObjectForView basic, String lifeStatus, Character LIFE)
  {
    this(basic.getName(), basic.getObjectContainedLst(), basic.getObjectAttributes(), lifeStatus,  basic.getFiles());
  }
 
  public StatusOfObjectForView(StatusOfObjectForView basic, String name, Double NAME)
  {
    this(name, basic.getObjectContainedLst(), basic.getObjectAttributes(), basic.getLifeStatus(),  basic.getFiles());
  }
  
  
  
  public StatusOfObjectForView(final String objName,  final String lifeStatus, final List<String> files, final List<String> objectsContained)
  {
    this(objName, objectsContained, null, lifeStatus, files);
  }

  public String getLifeStatus() {
    return lifeStatus;
  }

  public List<String> getFiles() {
    return files;
  }

  @Override
  public String toString()
  {
    String s= "";
    s = s + " Status of Life: " + lifeStatus;
    if(files != null && files.size() != 0)
    {
      s = "\n Files Contained: \n";
      for(String fileStr : this.files)
      {
        s = s + fileStr + "\n";
      }
    }
    else if (files == null)
    {
      s = s + "\n This object can't contain files";
    }
    else
    {
      s = s + "\n No files at the moment (but they could be present in the future)";
    }
    
    if(this.objContainedInside == null)
    {
      s = s + "\n the object can't contain files";
    }
    else if(this.objContainedInside != null && this.objContainedInside.size() == 0)
    {
      s = s + "\n the object does not contain objects but it could";
    }
    else
    {
      for(String oi : this.objContainedInside)
      {
        s = s + "\n object contained: \n" +
              oi;
      }
    }
    
    if(this.abilities == null)
    {
      s = s + "\nThis object can't have abilities - NULL -  Something is wrong indeed!!!";
    }
    else if (this.abilities.size() == 0)
    {
      s = s + "\nThis object hasen't got any ability";
    }
    else if (this.abilities.size() != 0)
    {
      s = s + "\n Abilities: \n\t " + this.abilities;
    }
    
    return s;

  }
  
  public List<String> getObjectContainedLst()
  {
    return this.objContainedInside;
  }
  
  public List<BuildingObjectAttribute> getObjectAttributes() {
    return this.attributes;
  }

  public String getName() {
    return this.objName;
  }

  public Set<ObjectAbility> getAbilities() {
    return abilities;
  }

  public void setAbilities(Set<ObjectAbility> abilities) {
    this.abilities = abilities;
  }


}
