package com.eteks.sweethome3d.swing.objstatus;

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

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.file.FileObject;
import com.eteks.sweethome3d.swing.ActionCoolFactory;
import com.eteks.sweethome3d.viewcontroller.HomeView.ActionType;

public class FileStatusPanel extends JPanel {

  private List<String> files = new ArrayList<String>();
  private JLabel generalTitleLab = new JLabel("These are the files stored in the device");
  
  private JPanel tablePanel;
  private TableFilePanel tableFilePanel;
  
  public FileStatusPanel()
  {
    BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
    
    generalTitleLab.setAlignmentY(TOP_ALIGNMENT);
    generalTitleLab.setAlignmentX(CENTER_ALIGNMENT);
    this.setLayout(box);
    this.add(generalTitleLab);
    this.add(Box.createRigidArea(new Dimension(0, 20)));
    
    JPanel buttonAndTable = this.borderedPanelCenterVertical(20, Color.GREEN);
    
    
    JPanel buttonPanel = this.borderedPanelCenterHorizontal(20, Color.magenta);
    
    Action actionAddFile = this.getActionAddFile();
    JButton addFileBtn = new JButton(actionAddFile);
    JLabel  addFileDescription = new JLabel("Click to Add a File");
    
    buttonPanel.add(addFileDescription);
    buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    buttonPanel.add(addFileBtn);
    
    tablePanel = this.borderedPanelCenterVertical(20, Color.ORANGE);
    
    
    
    buttonAndTable.add(buttonPanel);
    buttonAndTable.add(tablePanel);
    
    
    this.add(buttonAndTable);
    this.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
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
  
  public void setFileStatus(List<String> files) {
   
    if(files == null)
    {
      throw new IllegalStateException("files can't be null, at least emply list!");
    }
    
    this.files = files;
   
   List<String> filesMut = new ArrayList<String>();

   
   filesMut.addAll(this.files);
   
   this.tableFilePanel = new TableFilePanel(filesMut);
   this.tablePanel.add(this.tableFilePanel);
  }
  
  public List<String> getFiles()
  {
    if(this.tableFilePanel != null)
        return this.tableFilePanel.getFiles();
    else
      return null;
  }
  

  private JPanel borderedPanelCenterHorizontal(int bord, Color color)
  {
    JPanel jp = new JPanel();
    jp.setBorder(BorderFactory.createEmptyBorder(bord, bord, bord, bord));
    jp.setAlignmentX(CENTER_ALIGNMENT);
    jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
    jp.setBackground(color);
    return jp;
  }
  
  
  private JPanel borderedPanelCenterVertical(int bord, Color color)
  {
    JPanel jp = new JPanel();
    jp.setBorder(BorderFactory.createEmptyBorder(bord, bord, bord, bord));
    jp.setAlignmentX(CENTER_ALIGNMENT);
    jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
    jp.setBackground(color);
    return jp;
  }
  

}
