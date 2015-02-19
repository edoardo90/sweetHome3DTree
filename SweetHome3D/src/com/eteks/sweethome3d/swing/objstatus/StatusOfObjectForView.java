package com.eteks.sweethome3d.swing.objstatus;

import java.util.Collections;
import java.util.List;

public  class StatusOfObjectForView
{
  private final String lifeStatus;
  private final List<String> files;
  
  public StatusOfObjectForView(final String lifeStatus, final List<String> files)
  {
    this.lifeStatus =  lifeStatus;
    if(files == null)
    {
      this.files = null;
    }
    else
    {
      this.files = Collections.unmodifiableList(files);
    }
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
  
  
}
