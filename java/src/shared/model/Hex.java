package shared.model;

import shared.definitions.HexType;
import shared.locations.HexLocation;

/**
 * Hex class.
 */
public class Hex {
	private HexType resource;
	private HexLocation location;
	private int number;

	public HexType getResource() {
		return resource;
	}

	public int getNumber() {
		return number;
	}

	public HexLocation getLocation() {
		return location;
	}
	
}



