package com.eteks.sweethome3d.swing.objstatus;

import java.util.ArrayList;
import java.util.List;

import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.JPanelColor;

public abstract class JPanelStatusDecorator extends JPanelColor {

  protected StatusOfObjectForView initialStatusObjectForView;
  private List<JPanelStatusDecorator> decorated = new ArrayList<JPanelStatusDecorator>();

  /**
   * <pre>
   * Assign initialStatusObjectForView variable, used for the view 
   * Calls  drawOnPanel method: the method, inheredited by all sons of this class,
   * adds components on this panel
   * 
   * </pre>
   * @param decoratedPanel
   * @param initialStatusObjectForView
   */
  public JPanelStatusDecorator(JPanelStatusDecorator panelToDecore, String name, StatusOfObjectForView status) 
  {
    super(name);
    this.initialStatusObjectForView = status;
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
    this.revalidate();

  } 

  public JPanelStatusDecorator(String name)
  {
    super(name);
    this.initialStatusObjectForView = null;
  }

  protected  abstract StatusOfObjectForView getOwnStatus();

  public StatusOfObjectForView getComplexiveStatus()
  {

    StatusOfObjectForView totalStatus = new StatusOfObjectForView(null, null);
    for(JPanelStatusDecorator dec : this.decorated)
    {
      StatusOfObjectForView st =  dec.getOwnStatus();
      
      if(st != null)
      {    
        if(st.getLifeStatus() != null)
          totalStatus = new StatusOfObjectForView(totalStatus, st.getLifeStatus(), 42.0);
        if(st.getFiles() != null)
          totalStatus = new StatusOfObjectForView(totalStatus, st.getFiles(), "42");
        if(st.getObjectContainedLst() != null)
          totalStatus = new StatusOfObjectForView(totalStatus, st.getObjectContainedLst(), new Boolean(true));
        if(st.getObjectAttributes() != null)
          totalStatus = new StatusOfObjectForView(totalStatus, st.getObjectAttributes(), 42);
      }
    }
    return totalStatus;
  }

  public abstract void addSpecificComponent();
}
