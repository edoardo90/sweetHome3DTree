package com.eteks.sweethome3d.swing.opendialog;
import java.io.File;

import javax.swing.filechooser.FileFilter;

/* IfcFilter.java is used by FileIfcDialog.java. */
public class IfcFilter extends FileFilter {

  //Accept all directories and all gif, jpg, tiff, or png files.
  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    }

    String extension = Utils.getExtension(f);
    if (extension != null) {
       return extension.equals(Utils.ifc); 
    }

    return false;
  }

  //The description of this filter
  public String getDescription() {
    return "Ifc (2.3) Files";
  }
}
