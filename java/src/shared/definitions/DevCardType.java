package shared.definitions;

public enum DevCardType
{
	
	SOLDIER, YEAR_OF_PLENTY, MONOPOLY, ROAD_BUILD, MONUMENT;
	
	public static String getCamelCase(DevCardType devCard) {
		switch (devCard) {
			case SOLDIER:
				return "soldier";
			case YEAR_OF_PLENTY:
				return "yearOfPlenty";
			case MONOPOLY:
				return "monopoly";
			case ROAD_BUILD:
				return "roadBuilding";
			case MONUMENT:
				return "monument";
			default:
				return "";
		}	
	}
}



