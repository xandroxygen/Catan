package client.model;

import shared.locations.EdgeLocation;
/**
 * Road class
 */
public class Road {
	public Road(){}

	public Road(EdgeLocation location, int owner) {
		this.location = location;
		this.owner = owner;
	}

	private EdgeLocation location;
	private int owner;

	public EdgeLocation getLocation() {
		return location.getNormalizedLocation();
	}
	
	public int getOwnerIndex() {
		return owner;
	}
}
