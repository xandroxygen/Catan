package shared.model;

import com.google.gson.JsonObject;

import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
/**
 * Road class
 */
public class Road {
	
	private EdgeLocation location;
	private int owner;
	
	public Road(){}

	public Road(EdgeLocation location, int owner) {
		this.location = location;
		this.owner = owner;
	}
	
	public Road(JsonObject roadJson) {
    	try {
	    	owner = roadJson.get("owner").getAsInt();
	    	JsonObject locationJSON = roadJson.getAsJsonObject("location");
	    	EdgeDirection dir = EdgeDirection.getEnumFromAbbrev(locationJSON.get("direction").getAsString().toUpperCase());

	    	HexLocation hex = new HexLocation(locationJSON.get("x").getAsInt(),locationJSON.get("y").getAsInt());
	    	location = new EdgeLocation(hex,dir);
    	}
    	catch (Exception e) {
    		// No resource
    	}
    }

	public EdgeLocation getLocation() {
		return location.getNormalizedLocation();
	}
	
	public int getOwnerIndex() {
		return owner;
	}
}
