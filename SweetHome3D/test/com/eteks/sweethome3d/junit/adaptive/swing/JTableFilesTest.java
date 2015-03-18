package com.eteks.sweethome3d.junit.adaptive.swing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.adaptive.security.assets.LifeStatus;
import com.eteks.sweethome3d.adaptive.security.assets.PCObject;
import com.eteks.sweethome3d.adaptive.security.assets.file.FileObject;
import com.eteks.sweethome3d.adaptive.security.assets.file.NonDisclose;
import com.eteks.sweethome3d.adaptive.security.assets.file.SecurityLevel;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.io.DefaultUserPreferences;
import com.eteks.sweethome3d.junit.adaptive.BasicTest;
import com.eteks.sweethome3d.junit.adaptive.RoomCentroidTest.ControllerTest;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.LengthUnit;
import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.swing.SwingViewFactory;
import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;
import com.eteks.sweethome3d.viewcontroller.HomeView;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

public class JTableFilesTest extends BasicTest {

  @Override
  public void doStuffInsideMain(Home home, UserPreferences preferences) {
  }
  
  
  
  public void doOtherInMain(Home home, UserPreferences preferences, HomeView hv)
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
    
    
    FileObject fob2 = new FileObject(this.getFile("Add16.gif"));
    FileObject fob3 = new FileObject(this.getFile("test.obj"));
    FileObject fob4 = new FileObject(this.getFile("test.dae"));
    FileObject fob5 = new FileObject(this.getFile("words.txt"));
    FileObject[] fobs = new FileObject[]{fob2, fob3,fob4, fob5};
    for(int i=0; i<4;i++)
    {
      files.add(fobs[i].getFileRepresentation());
    }
    
    StatusOfObjectForView status = new StatusOfObjectForView(lifeStatus, files);
    pc1.setStatusFromView(status);
    
    
    hv.showStatusDialog(status, true);
  }
  
  
  
  public  static void main(String [] args) {
    ViewFactory viewFactory = new SwingViewFactory();
    UserPreferences preferences = new DefaultUserPreferences();
    preferences.setUnit(LengthUnit.METER);
    Home home = new Home();
    ControllerTest t = new ControllerTest(home, preferences, viewFactory);
    HomeView hv = t.getView();
    JTableFilesTest jt = new JTableFilesTest();
    jt.doStuffInsideMain(home, preferences);
    jt.doOtherInMain(home, preferences, hv);
  }
  
  
  

}
