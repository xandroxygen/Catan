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

	public Hex(HexType resource, HexLocation location, int number) {
		super();
		this.resource = resource;
		this.location = location;
		this.number = number;
	}

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



