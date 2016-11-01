package shared.locations;

public enum VertexDirection
{
	West, NorthWest, NorthEast, East, SouthEast, SouthWest;
	
	private VertexDirection opposite;
	
	static
	{
		West.opposite = East;
		NorthWest.opposite = SouthEast;
		NorthEast.opposite = SouthWest;
		East.opposite = West;
		SouthEast.opposite = NorthWest;
		SouthWest.opposite = NorthEast;
	}
	
	public VertexDirection getOppositeDirection()
	{
		return opposite;
	}
	
	public static VertexDirection getEnumFromAbbrev(String abbrev) {
		switch (abbrev) {
		case "NW":
			return NorthWest;
		case "W":
			return West;
		case "NE":
			return NorthEast;
		case "E":
			return East;
		case "SW":
			return SouthWest;
		case "SE":
			return SouthEast;
		default:
			return NorthWest;
		}
	}
}

