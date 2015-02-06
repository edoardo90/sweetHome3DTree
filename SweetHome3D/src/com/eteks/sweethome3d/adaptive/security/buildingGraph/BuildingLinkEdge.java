package com.eteks.sweethome3d.adaptive.security.buildingGraph;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingGraphPart;

public class BuildingLinkEdge extends BuildingGraphPart{

	private String firstRoom;
	private String secondRoom;
	
	public BuildingLinkEdge( String room1, String room2)
	{
	     this.firstRoom = room1;
	     this.secondRoom = room2;
	}
	
	public boolean makeSense()
	{
		return !  firstRoom.equals(secondRoom);
	}
	
	@Override
	public String toString()
	{
	  return firstRoom + "<------>" +  secondRoom;
	}
	
}
