package shared.model;

import java.io.Serializable;

import shared.locations.HexLocation;

/**
 * Robbing Class
 */
public class Robber implements Serializable {
	private HexLocation location;
	
	public Robber(int x, int y) {
		location = new HexLocation(x,y);
	}

	public Robber() {
		location = new HexLocation(0,-2);
	}

	public HexLocation getLocation() {
		return location;
	}

	public void setLocation(HexLocation location) {
		this.location = location;
	}
}
