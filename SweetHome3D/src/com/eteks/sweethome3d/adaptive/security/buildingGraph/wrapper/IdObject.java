package com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper;

public class IdObject {
  private String idObject;

  @Override
  public String toString()
  {
    return "ID: " + idObject;
  }
  
  
  public IdObject(String idObject)
  {
    this.idObject = idObject;
  }
  public String getIdObject() {
    return idObject;
  }

  public void setIdObject(String idObject) {
    this.idObject = idObject;
  }
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.idObject == null)
        ? 0
        : this.idObject.hashCode());
    return result;
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    IdObject other = (IdObject)obj;
    if (this.idObject == null) {
      if (other.idObject != null)
        return false;
    } else if (!this.idObject.equals(other.idObject))
      return false;
    return true;
  }
  
  
  
}
