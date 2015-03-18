package com.eteks.sweethome3d.adaptive.security.assets.file;

import java.io.File;
import java.io.Serializable;


public class FileObject implements Serializable  {
  
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
    
    try{
        this.securityLevel = SecurityLevel.valueOfSmarter(fileLev);
        this.discloseLevel = NonDisclose.valueOfSmarter(fileNDA);
        if(this.securityLevel == null)
          throw new Exception();
    }
    catch(Exception e) //switch
    {
      this.securityLevel = SecurityLevel.valueOfSmarter(fileNDA);
      this.discloseLevel = NonDisclose.valueOfSmarter(fileLev);
    }
    
    if(this.securityLevel == null || this.discloseLevel == null)
    {
      throw new IllegalStateException("the string was bad!! :::  " + fileObjAsString  + " ... i could not compute it !");
    }
    
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
        this.getDiscloseLevel().name()
        + 
        "," +
        this.getSecurityLevel().name()
        ;
  }

  public String getFileRepresentationForTable() {
    return this.actualFile.getAbsolutePath() +
        "," + 
        this.getSecurityLevel().name()
        + 
        "," +
        this.getDiscloseLevel().name()
        ;
  }
  
  

}
