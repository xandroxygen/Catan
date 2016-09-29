package shared.locations;

import com.google.gson.annotations.SerializedName;

public enum EdgeDirection
{
	
	NorthWest, North, NorthEast, SouthEast, South, SouthWest;
	
	private EdgeDirection opposite;
	
	static
	{
		NorthWest.opposite = SouthEast;
		North.opposite = South;
		NorthEast.opposite = SouthWest;
		SouthEast.opposite = NorthWest;
		South.opposite = North;
		SouthWest.opposite = NorthEast;
	}
	
	public EdgeDirection getOppositeDirection()
	{
		return opposite;
	}
	
	public static EdgeDirection getEnumFromAbbrev(String abbrev) {
		switch (abbrev) {
		case "NW":
			return NorthWest;
		default:
			return NorthWest;
		}
	}
}

