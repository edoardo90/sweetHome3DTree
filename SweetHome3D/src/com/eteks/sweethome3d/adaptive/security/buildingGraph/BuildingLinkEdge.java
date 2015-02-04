package com.eteks.sweethome3d.adaptive.security.buildingGraph;

import com.eteks.sweethome3d.adaptive.security.buildingGraphObjects.BuildingGraphPart;
import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public class BuildingLinkEdge extends BuildingGraphPart{

	private String firstRoom;
	private String secondRoom;
	
	public BuildingLinkEdge(Vector3D position, String room1, String room2)
	{
	     super(position);
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
