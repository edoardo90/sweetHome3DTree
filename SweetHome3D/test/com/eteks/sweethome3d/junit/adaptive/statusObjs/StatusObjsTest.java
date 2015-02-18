package com.eteks.sweethome3d.junit.adaptive.statusObjs;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.LifeStatus;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.PCObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.PrinterObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.FileObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.NonDisclose;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.SecurityLevel;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.adaptive.BasicTest;
import com.eteks.sweethome3d.junit.adaptive.DisplayRoomUnitsSITest;
import com.eteks.sweethome3d.junit.adaptive.FurnitureAddTest.ControllerTest;
import com.eteks.sweethome3d.junit.resources.ResTest;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.SwingViewFactory;
import com.eteks.sweethome3d.swing.objstatus.FrameStatus.StatusOfObjectForView;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

public class StatusObjsTest extends BasicTest {
  
  public void testRepresentationGET()
  {
    Vector3D p1 = new Vector3D(0, 0, 0);
    Vector3D p2 = new Vector3D(100, 20, 0);
     
    PCObject pc1 = new PCObject(p1);
    PrinterObject printer1 = new PrinterObject(p2);
    
    FileObject secretProtectedFOB = new FileObject();
    File ff1 = getFile("test.obj");
    secretProtectedFOB.setActualFile(ff1);
    secretProtectedFOB.setSecurityLevel(SecurityLevel.SECRET);
    secretProtectedFOB.setDiscloseLevel(NonDisclose.NDA_PROTECTED);
    
    File ff2 = getFile("Delete16.gif");
    FileObject confidNonProtectedFOB = new  FileObject(ff2);
    confidNonProtectedFOB.setSecurityLevel(SecurityLevel.CONFIDENTIAL);
    confidNonProtectedFOB.setDiscloseLevel(NonDisclose.NOT_NDA_PROTECTED);
    
    pc1.addFile     (secretProtectedFOB);
    printer1.addFile(secretProtectedFOB);
    printer1.addFile(confidNonProtectedFOB);
    
    pc1.setLifeStatus(LifeStatus.OFF);
    
    
    StatusOfObjectForView represPC =  pc1.getStatusForView();
    List<String> fileStringsPC = represPC.getFiles();
    String statusPC = represPC.getLifeStatus();
    
    LifeStatus lifeStatusPC = LifeStatus.valueOf(statusPC);
    assertTrue("PC should be off!", lifeStatusPC == LifeStatus.OFF);
    
    String fileString1Pc1 = fileStringsPC.get(0);
    
    FileObject fob1Pc1 = new FileObject(fileString1Pc1);

    
    assertTrue("File 1 of PC should be SECRET and NOT_NDA",
        check(fob1Pc1, SecurityLevel.SECRET, NonDisclose.NDA_PROTECTED));
    
    
    StatusOfObjectForView represPrinter =  printer1.getStatusForView();
    List<String> fileStringsPrinter = represPrinter.getFiles();
    String statusPrinter = represPrinter.getLifeStatus();
    LifeStatus lifePrinter = LifeStatus.valueOf(statusPrinter);
    assertTrue("printer should be on!", lifePrinter == LifeStatus.ON);
    
    String file1PrinterStr = fileStringsPrinter.get(0);
    String file2PrinterStr = fileStringsPrinter.get(1);
    FileObject printerFob1 = new FileObject(file1PrinterStr);
    FileObject printerFob2 = new FileObject(file2PrinterStr);
    
    assertTrue("File 1 of Printer should be SECRET and NOT_NDA",
        check(printerFob1, SecurityLevel.SECRET, NonDisclose.NDA_PROTECTED));
    
    assertTrue("File 2 of Printer should be CONFIDENTIAL and NDA",
        check(printerFob2, SecurityLevel.CONFIDENTIAL, NonDisclose.NOT_NDA_PROTECTED));
    
  }
  
  public void testRepresentationSET()
  {
    
    PCObject pc1 = new PCObject(new Vector3D(0,0,0));
    
    List<String> files = new ArrayList<String>();
    String lifeStatus = LifeStatus.BROKEN.name();
    
    File ff1 = getFile("test.obj");
    String file1AbsPath = ff1.getAbsolutePath();
    System.out.println(file1AbsPath);
    
    SecurityLevel secLev = SecurityLevel.UNCLASSIFIED;
    NonDisclose  ndaLev = NonDisclose.NOT_NDA_PROTECTED;
    FileObject  fob1 = new FileObject(ff1);
    fob1.setDiscloseLevel(ndaLev);
    fob1.setSecurityLevel(secLev);
    
    String fob1Represent =  fob1.getFileRepresentation();
    
    assertEquals("represent wrong!", file1AbsPath + "," + ndaLev.name()  + "," +  secLev.name() 
        , fob1Represent);
    files.add(fob1Represent);
    
    
    StatusOfObjectForView status = new StatusOfObjectForView(lifeStatus, files);
    pc1.setStatusFromView(status);
    
    LifeStatus  file1Status =  pc1.getLifeStatus();
    assertTrue("file1Status" , file1Status == LifeStatus.BROKEN );
    
    FileObject fob1PC = pc1.getActiveFiles().get(0);
    assertTrue("should be UNCLASSIFIED, NON_NDA",
               check(fob1, SecurityLevel.UNCLASSIFIED, NonDisclose.NOT_NDA_PROTECTED));
    String absolutePath = fob1PC.getActualFile().getAbsolutePath();
    
    assertEquals("", file1AbsPath, absolutePath); 
    
    
  }
  
  
  
  private boolean check(FileObject fob, SecurityLevel sec, NonDisclose nda)
  {
    return fob.getSecurityLevel() == sec && fob.getDiscloseLevel() == nda;
  }
  
    
  
  @Override
  public void doStuffInsideMain(Home home, UserPreferences preferences) {
  
  }
  
  public  static void main(String [] args) {
    ViewFactory viewFactory = new SwingViewFactory();
    UserPreferences preferences = new DefaultUserPreferences();
    preferences.setUnit(LengthUnit.METER);
    Home home = new Home();
    ControllerTest t = new ControllerTest(home, preferences, viewFactory);

    StatusObjsTest f = new StatusObjsTest();
    f.doStuffInsideMain(home, preferences);

  }
  
  
}
