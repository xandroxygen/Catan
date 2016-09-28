package client.model;

import shared.locations.EdgeLocation;
/**
 * Road class
 */
public class Road {
	private EdgeLocation location;
	private int owner;
	// TODO: This class might need customer desrialization depending on how smart Gson is.

	public EdgeLocation getLocation() {
		return location.getNormalizedLocation();
	}
}
