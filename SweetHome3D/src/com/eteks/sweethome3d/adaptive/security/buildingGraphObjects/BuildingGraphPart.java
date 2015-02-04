package com.eteks.sweethome3d.adaptive.security.buildingGraphObjects;

import com.eteks.sweethome3d.adaptive.security.parserobjects.Vector3D;

public abstract class BuildingGraphPart {

	protected String id;
	protected String name;
	private final Vector3D position;
	
	public String getName() {
		return name;
	}

	public String getId()
	{
		return this.id;
	}
	
	
	public  BuildingGraphPart(Vector3D position) {
            this.position = position;
        }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BuildingGraphPart other = (BuildingGraphPart) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
  public Vector3D getPosition() {
    return position;
  }

	
}
