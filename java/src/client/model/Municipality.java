package client.model;

import shared.locations.VertexLocation;

public abstract class Municipality {
	private VertexLocation location;
	private int owner;
	
	public Municipality(VertexLocation location, int owner) {
		this.location = location;
		this.owner = owner;
	}

	public VertexLocation getLocation() {
		return location.getNormalizedLocation();
	}
	
	public int getOwnerIndex() {
		return owner;
	}
}
