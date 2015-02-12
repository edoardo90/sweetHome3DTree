package com.eteks.sweethome3d.adaptive.security.buildingGraph.wrapper;

public class IdRoom {
  private String idRoom;

  public IdRoom(String idRoom)
  {
    this.idRoom = idRoom;
  }
  public String getIdRoom() {
    return idRoom;
  }

  public void setIdRoom(String idRoom) {
    this.idRoom = idRoom;
  }
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.idRoom == null)
        ? 0
        : this.idRoom.hashCode());
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
    IdRoom other = (IdRoom)obj;
    if (this.idRoom == null) {
      if (other.idRoom != null)
        return false;
    } else if (!this.idRoom.equals(other.idRoom))
      return false;
    return true;
  }
  
  
  
}
