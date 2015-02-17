package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file;

import java.io.File;


public class FileObject  {
  
  private File actualFile;
  private SecurityLevel securityLevel;
  private NonDisclose   discloseLevel;
  
  public FileObject() {}
  
  public FileObject(File file)
  {
    this.actualFile = file;
  }
  
  public File getActualFile() {
    return actualFile;
  }
  public void setActualFile(File actualFile) {
    this.actualFile = actualFile;
  }
  public SecurityLevel getSecurityLevel() {
    return securityLevel;
  }
  public void setSecurityLevel(SecurityLevel securityLevel) {
    this.securityLevel = securityLevel;
  }
  public NonDisclose getDiscloseLevel() {
    return discloseLevel;
  }
  public void setDiscloseLevel(NonDisclose discloseLevel) {
    this.discloseLevel = discloseLevel;
  }
  
  

}
