package com.eteks.sweethome3d.swing.objstatus.framestatus;

import java.util.List;

import javax.swing.JFrame;

import com.eteks.sweethome3d.adaptive.security.buildingGraph.policy.ABACPolicy;
import com.eteks.sweethome3d.swing.objstatus.statuspanels.JPanelPolicy;

public class FrameStatusPolicy extends FrameStatusAbstractStateLess{

  private static final long serialVersionUID = 8717312887290271582L;
  private List<ABACPolicy> policies;
  private JPanelPolicy  panelPolicies;
  
  public FrameStatusPolicy(JFrame parent, String name, List<ABACPolicy> policies) {
    super(parent, name);
    this.policies = policies;
    this.addComps();
    this.pack();
    
  }
  private void addComps()
  {
    this.panelPolicies = new JPanelPolicy(this.policies);

    //TODO: predefine values...
    
    
    this.add(this.panelPolicies);
  }

  public List<ABACPolicy> getPolicies() {
    return  this.panelPolicies.getRows();
  }

  public void setPolicy(List<ABACPolicy> policies) {
    this.policies = policies;
  }


}
