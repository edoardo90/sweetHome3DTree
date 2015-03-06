package com.eteks.sweethome3d.swing.objstatus.statuspanels;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import scala.Array;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.FileObject;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.NonDisclose;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.SecurityLevel;
import com.eteks.sweethome3d.swing.ActionCoolFactory;
import com.eteks.sweethome3d.swing.objstatus.tables.TableFilePanel;
import com.eteks.sweethome3d.viewcontroller.HomeView.ActionType;

public class FileStatusPanel extends JPanelColorStatefull<String> {

  private List<String> files = new ArrayList<String>();
  private String title = "These are the files stored in the device";
  private JLabel generalTitleLab = new JLabel(title);
  
  private JPanel tablePanel;
  private TableFilePanel tableFilePanel;
  
  public FileStatusPanel(String name)
  {
    super(name);
    BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
    
    generalTitleLab.setAlignmentY(TOP_ALIGNMENT);
    generalTitleLab.setAlignmentX(CENTER_ALIGNMENT);
    this.setLayout(box);
    
     
    JPanel buttonAndTable = this.borderedPanelCenterVertical(0, Color.GREEN);
    JPanel buttonPanel = super.borderedPanelCenterHorizontal(10, Color.magenta);
    
    Action actionAddFile = this.getActionAddFile();
    JButton addFileBtn = new JButton(actionAddFile);
    JLabel  addFileDescription = new JLabel("Click to Add a File");
    
    buttonPanel.add(addFileDescription);
    buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    buttonPanel.add(addFileBtn);
    
    tablePanel = this.borderedPanelCenterVertical(10, Color.ORANGE);
    
    
    buttonAndTable.add(buttonPanel);
    buttonAndTable.add(tablePanel);
    
    
    this.add(buttonAndTable);
    Border bspace = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    this.setBorder(BorderFactory.createTitledBorder(bspace, title));
  }
  
  private Action getActionAddFile()
  {
    ActionCoolFactory acf = new ActionCoolFactory();
    Action a = acf.createAction(ActionType.ADD_FILE, this, "addFile");
    return a;
  }
  
  public void addFile()
  {
    final JFileChooser fc = new JFileChooser();
    
    int returnVal = fc.showOpenDialog(FileStatusPanel.this);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        FileObject fob = new FileObject(file);
        this.tableFilePanel.addRow(fob.getFileRepresentationForTable());
    } else {
        
    }
     
  }
  
  @Override
  public void setStatus(List<String> files) {
    
    if(files == null)
    {
      this.files = new ArrayList<String>();
    }
    else
    {  
       this.files = files;
    }
   
   List<String> filesMut = new ArrayList<String>();

   
   filesMut.addAll(this.files);
   
   this.tableFilePanel = new TableFilePanel(filesMut);
   this.setColumnFieldsEnum();
   
   this.tablePanel.add(this.tableFilePanel);
  }
  
  private void setColumnFieldsEnum() {
    this.tableFilePanel.setColumnEnum(null);
    this.tableFilePanel.setColumnEnum(SecurityLevel.class);
    this.tableFilePanel.setColumnEnum(NonDisclose.class);
    
  }

  public List<String> getFiles()
  {
    if(this.tableFilePanel != null)
        return this.tableFilePanel.getRows();
    else
      return null;
  }


  

}
