package com.eteks.sweethome3d.swing.objstatus.statuspanels;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.BuildingSecurityGraph;
import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingObjectContained;
import com.eteks.sweethome3d.swing.objstatus.JPanelStatusDecorator;
import com.eteks.sweethome3d.swing.objstatus.JStatusContainPanelDec;
import com.eteks.sweethome3d.swing.objstatus.JStatusContainPanelDec.ContPanelAim;
import com.eteks.sweethome3d.swing.objstatus.JStatusDumb;
import com.eteks.sweethome3d.swing.objstatus.JStatusFilePanelDec;
import com.eteks.sweethome3d.swing.objstatus.JStatusLifePanelDec;
import com.eteks.sweethome3d.swing.objstatus.framestatus.FrameStatusAbstract;
import com.eteks.sweethome3d.swing.objstatus.framestatus.FrameStatusPlain;
import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;
import com.eteks.sweethome3d.swing.objstatus.tables.TableContainmentPanel;

public class ContainementStatusPanel extends JPanelColor implements ActionListener {
  private static final long serialVersionUID = -3380325228386416664L;
  
  private JButton addObjectBtn = new JButton("Add Objects Contained");

  private TableContainmentPanel tableOfObjectsContained;
  
  public ContainementStatusPanel(String name) {
    super(name);
    BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
    this.setLayout(box);
    
    Border bspace = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    this.setBorder(BorderFactory.createTitledBorder(bspace, "Containment"));
  }
  
  public void setStatus(List<String> objsContained)
  {
    this.populateTable(objsContained);
  }
  
  private void populateTable(List<String> objsCont)
  {
    tableOfObjectsContained = new TableContainmentPanel(objsCont);   
    this.add(tableOfObjectsContained);
  }

  public void actionPerformed(ActionEvent e) {
     
    List<String> objectsContainedSelected = this.getContainedObjectsSelected();
    System.out.println(objectsContainedSelected);
    this.tableOfObjectsContained.addAllRows(objectsContainedSelected);
  }
  
  private List<String> getContainedObjectsSelected()
  {
    JFrame f = (JFrame)  JOptionPane.getFrameForComponent((JComponent) this.getParent());
    
    List<String> lstAllPossibleObjects = getAllPossibleObjCont();
    
    JPanelStatusDecorator panelAllObjectContainable = new JStatusDumb("contained");
    panelAllObjectContainable = new JStatusContainPanelDec(panelAllObjectContainable,
        new StatusOfObjectForView(lstAllPossibleObjects, null, null, null), ContPanelAim.PICK_OBJECTS_FROM_ALL);
    
    FrameStatusAbstract fs = new FrameStatusPlain
               (panelAllObjectContainable, f, "Edit Status");
    fs.setLocation(400, 200);
    fs.setVisible(true);

    StatusOfObjectForView r = fs.getRepresentation();
    return r.getObjectContainedLst();
  }

  private List<String> getAllPossibleObjCont() {
    BuildingSecurityGraph segraph = BuildingSecurityGraph.getInstance();
    return segraph.getListStrContainedObjects();
    
  }

  public void addButtonAddObject() {
    this.addObjectBtn.setAlignmentX(CENTER_ALIGNMENT);
    this.addObjectBtn.addActionListener(this);
    this.add(this.addObjectBtn);  
  }

  public List<String> getSelectedContObjects() {
    
    return this.tableOfObjectsContained.getSelectedRows();
  }

  public List<String> getAllRows() {
    return this.tableOfObjectsContained.getRows();
  }
  
  
}
