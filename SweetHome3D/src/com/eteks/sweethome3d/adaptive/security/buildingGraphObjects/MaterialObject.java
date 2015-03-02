package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;

public abstract class MaterialObject extends BuildingObjectContained {

  private LifeStatus lifeStatus = LifeStatus.ON;
  
  public MaterialObject(Vector3D position) {
    super(position);
    
  }

  public LifeStatus getLifeStatus() {
    return lifeStatus;
  }

  public void setLifeStatus(LifeStatus lifeStatus) {
    this.lifeStatus = lifeStatus;
  }

  @Override
  public StatusOfObjectForView getStatusForView() {
    return new StatusOfObjectForView(this.getName(),
                                     this.getObjectConainedStr(), 
                                     this.getAttributes(),
                                     "" + lifeStatus, null);
  }

  @Override
  public void setStatusFromView( StatusOfObjectForView status) {
    String statusLife = status.getLifeStatus();
    LifeStatus lifeSt = LifeStatus.valueOfSmarter(statusLife);
    this.setLifeStatus(lifeSt);
    this.setObjectsContainedFromView(status);
  }
  
  @Override
  public String toString()
  {
    return super.toString() + 
        "\n\t\t" + this.getStatusForView().toString().replace("\n", "\n\t\t");
        
  }

}
