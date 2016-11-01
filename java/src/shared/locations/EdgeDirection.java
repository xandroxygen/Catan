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
		case "N":
			return North;
		case "NE":
			return NorthEast;
		case "S":
			return South;
		case "SW":
			return SouthWest;
		case "SE":
			return SouthEast;
		default:
			return NorthWest;
		}
	}
}

