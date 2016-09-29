package client.model;

import shared.locations.HexLocation;

/**
 * Robber Class
 */
public class Robber {
	private HexLocation location;
	
	public Robber(int x, int y) {
		location = new HexLocation(x,y);
	}

	public HexLocation getLocation() {
		return location;
	}
}
