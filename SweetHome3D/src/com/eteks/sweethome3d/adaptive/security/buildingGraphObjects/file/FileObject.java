package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file;

import java.io.File;


public class FileObject  {
  
  private File actualFile;
  private SecurityLevel securityLevel;
  private NonDisclose   discloseLevel;
  
  public FileObject() {
    this.securityLevel = SecurityLevel.UNCLASSIFIED;
    this.discloseLevel = NonDisclose.NOT_NDA_PROTECTED;
  }
  
  public FileObject(File file)
  {
    this();
    this.actualFile = file;
  }
  
  public FileObject(String fileObjAsString) {
    String [] strings = fileObjAsString.split(",");
    if(strings.length != 3)
      throw new IllegalStateException("FileHolder - String should be FILE,NDA,LEV format");
    
    String fileName = strings[0];
    String fileNDA = strings[1];   
    String fileLev = strings[2];
    this.securityLevel = SecurityLevel.valueOf(fileLev);
    this.discloseLevel = NonDisclose.valueOf(fileNDA);
    
    File f = new File(fileName);
    this.actualFile = f;
    
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

  public String getFileRepresentation() {

    return this.actualFile.getAbsolutePath() +
        "," + 
        this.getDiscloseLevel().name() + 
        "," +
        this.getSecurityLevel().name() ;
  }
  
  

}
