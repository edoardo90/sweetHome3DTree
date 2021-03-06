package com.eteks.sweethome3d.adaptive.security.assets;

import java.util.HashSet;
import java.util.Set;

import com.eteks.sweethome3d.adaptive.security.assets.attributes.BuildingObjectAttribute;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;

public abstract class MaterialObject extends Asset {

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
    StatusOfObjectForView st =  new StatusOfObjectForView(this.getName(),
                                     this.getObjectConainedStr(), 
                                     this.getAttributes(),
                                     "" + lifeStatus, null);
    st.setAbilities(this.getAbilities());
    return st;
  }

  @Override
  public void setStatusFromView( StatusOfObjectForView status) {
    String statusLife = status.getLifeStatus();
    LifeStatus lifeSt = LifeStatus.valueOfSmarter(statusLife);
    this.setLifeStatus(lifeSt);
    this.setObjectsContainedFromView(status);
    this.setAttributesFromView(status);
  }
  
  private void setAttributesFromView(StatusOfObjectForView status)
  {
    this.clearAttributes();
    Set<BuildingObjectAttribute> attrs = new HashSet<BuildingObjectAttribute>(status.getObjectAttributes());
    this.addAllAttributes(attrs);
  }
  
  @Override
  public String toString()
  {
    return super.toString() + 
        "\n\t\t" + this.getStatusForView().toString().replace("\n", "\n\t\t");
        
  }

}
