package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.FileObject;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.swing.objstatus.StatusOfObjectForView;

public class FileHolder extends MaterialObject implements Iterable<FileObject> {

  private List<FileObject> activeFiles = new ArrayList<FileObject>();
  
  public FileHolder(Vector3D position) {
    super(position);
    this.setLifeStatus(LifeStatus.ON);

  }
  
  public void addFile(FileObject file)
  {
    this.activeFiles.add(file);
  }

  public void removeFile(FileObject file)
  {
    this.activeFiles.remove(file);
  }
  
  public List<FileObject> getActiveFiles() {
    return activeFiles;
  }

  public void setActiveFiles(List<FileObject> activeFiles) {
    this.activeFiles = activeFiles;
  }

  public List<String> getActiveFilesStr()
  {
    List<String> files = new ArrayList<String>();
    for(FileObject f : this.getActiveFiles())
    {
      files.add(f.getFileRepresentation());
    }
    return files;
  }
  
  @Override
  public StatusOfObjectForView getStatusForView() {
    return new StatusOfObjectForView("" + getLifeStatus().name(), 
                                 getActiveFilesStr() );
  }
  
  @Override
  public void setStatusFromView( StatusOfObjectForView status) {
    super.setStatusFromView(status);
    
    List<String> filesStr = status.getFiles();
    for(String fileObjAsString : filesStr)
    {
      FileObject fob = new FileObject(fileObjAsString);
      this.addFile(fob);
    }
    
  }
  

  public Iterator<FileObject> iterator() {
   return new Iterator<FileObject>() {
    
    private List<FileObject> files = activeFiles;
    private int index = 0;
    
    public boolean hasNext() {
      try
      {
        activeFiles.get(index);
        return true;
      }
      catch(IndexOutOfBoundsException e)
      {
        return false;
      }
    
    }

    public FileObject next() {
       
      FileObject fo = this.files.get(index);
      index ++;
      return fo;
    }

    /**
     * Do not remove anything
     */
    public void remove() {
      
    }
  };
  }

}
