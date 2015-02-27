package com.eteks.sweethome3d.swing.objstatus;

import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.JPanelColor;

public abstract class JPanelStatusDecorator extends JPanelColor {
  
  protected StatusOfObjectForView initialStatusPanel;
  private List<JPanelStatusDecorator> decorated = new ArrayList<JPanelStatusDecorator>();
  
  /**
   * Assign initialStatusPanel variable and call drawOnPanel method
   * @param decoratedPanel
   * @param initialStatusPanel
   */
  public JPanelStatusDecorator(JPanelStatusDecorator panelToDecore, String name, StatusOfObjectForView status) 
  {
      super(name);
      this.initialStatusPanel = status;
      for(JPanelColor coloredP : panelToDecore.getPanels())
      {
        this.addPanel(coloredP, coloredP.getName());
        
      }
      this.decorated.add(this);
      if(panelToDecore.decorated.size() != 0)
      {
        this.decorated.addAll(panelToDecore.decorated);
      }
      this.addSpecificComponent();
      
  } 
  
  public JPanelStatusDecorator(String name)
  {
      super(name);
      this.initialStatusPanel = null;
  }
  
  protected  abstract StatusOfObjectForView getOwnStatus();

  public StatusOfObjectForView getComplexiveStatus()
  {
    int pippo=21;
    pippo++;
    StatusOfObjectForView totalStatus = new StatusOfObjectForView(null, null);
    for(JPanelStatusDecorator dec : this.decorated)
    {
       StatusOfObjectForView st =  dec.getOwnStatus();
       System.out.println("state : \n" + st);
       if(st != null)
       {    
         if(st.getFiles() != null)
                totalStatus = new StatusOfObjectForView(totalStatus.getLifeStatus(), st.getFiles());
         if(st.getLifeStatus() != null)
                 totalStatus = new StatusOfObjectForView(st.getLifeStatus(), totalStatus.getFiles());
       }
    }
    return totalStatus;
  }
 
  public abstract void addSpecificComponent();
}
