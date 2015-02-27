package com.eteks.sweethome3d.swing.objstatus.representation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;

public  class StatusOfObjectForView
{
  private final String lifeStatus;
  private final List<String> files;
  private final Map<BuildingObjectContained, BuildingObjectContained> objContains;

  public StatusOfObjectForView(  final Map<BuildingObjectContained, BuildingObjectContained> objContains,
                                 final String lifeStatus, final List<String> files)
  {
    this.lifeStatus =  lifeStatus;
    this.objContains = objContains;
    if(files == null)
    {
      this.files = null;
    }
    else
    {
      this.files = Collections.unmodifiableList(files);
    }
  }

  public StatusOfObjectForView(  final String lifeStatus, final List<String> files)
  {
    this(null, lifeStatus, files);
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
    return s;

  }

  public Map<BuildingObjectContained, BuildingObjectContained> getObjContains() {
    return objContains;
  }


}
