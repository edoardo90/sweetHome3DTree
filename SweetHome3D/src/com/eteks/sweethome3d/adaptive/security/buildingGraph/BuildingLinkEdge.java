package com.eteks.sweethome3d.adaptive.security.buildingGraph;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingGraphPart;

/**
 * This class generically define a link in the graph
 * 
 * The id  is referred to wall in case of wall without door, while it is referred to the door otherwise
 * 
 * @author Edoardo Pasi
 */

public abstract class BuildingLinkEdge extends BuildingGraphPart{

  protected String firstRoom;
  protected String secondRoom;

  /**
   * The generic constructor just ask for the 2 rooms, then wall and or door will be added 
   * depending on the actual dynamic type
   * @param room1
   * @param room2
   */
  public BuildingLinkEdge( String room1, String room2)
  {
    this.firstRoom = room1;
    this.secondRoom = room2;
  }

  public String getFirstRoom()
  {
    return this.firstRoom;
  }
  public String getSecondRoom()
  {
    return this.secondRoom;
  }


  @Override
  public String toString()
  {
    return firstRoom + "<------>" +  secondRoom + "\n";
  }


  @Override
  public boolean equals(Object other)
  {
    if(!(other instanceof BuildingLinkEdge))
    {
      return false;
    }
    else
    {
      BuildingLinkEdge linkOther = (BuildingLinkEdge) other;
      if(! (linkOther.getId().equals(this.getId())))
      {
        return false;
      }
      if(  (this.firstRoom.equals(linkOther.firstRoom) && this.secondRoom.equals(linkOther.secondRoom))  || 
          (this.firstRoom.equals(linkOther.secondRoom) && this.secondRoom.equals(linkOther.firstRoom))     )
      {
        return true;
      }


    }

    return false;
  }




}
