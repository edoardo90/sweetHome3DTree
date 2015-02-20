package com.eteks.sweethome3d.swing.opendialog;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FileIfcDialog extends JDialog
{
  private static final long serialVersionUID = 7089556007345572010L;
  private JFileChooser fc;
  private String name;
      public FileIfcDialog(JFrame parent, String name)
      {
        super(parent, name, true);
        this.name = name;
      }
  
    public String getFileAbsoluteName() {
        //Set up the file chooser.
        if (fc == null) {
            fc = new JFileChooser();
            
            fc.setFileFilter(new IfcFilter());
            fc.setAcceptAllFileFilterUsed(false);
        }

        //Show it.
        int returnVal = fc.showDialog(FileIfcDialog.this,
                                      name);

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) 
        {
            File file = fc.getSelectedFile();
            return file.getAbsolutePath();
        }

        //Reset the file chooser for the next time it's shown.
        fc.setSelectedFile(null);
        return null;
    }
}
