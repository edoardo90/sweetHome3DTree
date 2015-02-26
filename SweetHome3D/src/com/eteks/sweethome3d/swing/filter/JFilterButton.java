package com.eteks.sweethome3d.swing.filter;

import javax.swing.Action;

import com.eteks.sweethome3d.swing.ActionCoolFactory;
import com.eteks.sweethome3d.swing.JCurtainButton;
import com.eteks.sweethome3d.viewcontroller.HomeView.ActionType;

public class JFilterButton extends JCurtainButton {

  private static ActionCoolFactory acf= null;

  public JFilterButton(Action actionFatherButton) {
    super(actionFatherButton);
  }
  
  public JFilterButton()
  {
    super(getDefaultFatherAction());
  }
  
  private static ActionCoolFactory getActionFactory()
  {
    if(acf == null)
         acf = new ActionCoolFactory();
    return acf;
  }
  
  
  private static Action  getDefaultFatherAction()
  {
    Action a1;
    Object dumb= new Object();
    a1 =  getActionFactory().createAction(ActionType.FILTER, dumb, "toString");
    return a1;
  }
  

}
