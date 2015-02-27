package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;
import com.eteks.sweethome3d.model.PieceOfFurniture;
import com.eteks.sweethome3d.swing.objstatus.representation.StatusOfObjectForView;

public class CableObject extends BuildingObjectContained {

 
  public CableObject(Vector3D position) {
    super(position);
 
  }

  public PieceOfFurniture getPieceOfForniture() {
    //null means no representation 
    // TODO: how do we graphically represent cables?
    return null;
  }

  @Override
  public StatusOfObjectForView getStatusForView() {
    
    // TODO: how do we graphically represent cables?
    return null;
  }

  @Override
  public void setStatusFromView( StatusOfObjectForView s) {
    // TODO cable has no real state
    
  }

}
