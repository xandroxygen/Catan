package client.model;

import shared.locations.VertexLocation;

public abstract class Municipality {
	private VertexLocation location;
	private int owner;

	public VertexLocation getLocation() {
		return location.getNormalizedLocation();
	}
}
