package com.eteks.sweethome3d.swing.objstatus.statuspanels;

import java.util.List;

public abstract class JPanelColorStatefull<T> extends JPanelColor {

  public JPanelColorStatefull(String name) {
    super(name);
    // TODO Auto-generated constructor stub
  }


  public abstract void setStatus(List<T> statusList);

}
