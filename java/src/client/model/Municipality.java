package client.model;

import com.google.gson.JsonObject;

import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

public abstract class Municipality {
	private VertexLocation location;
	private int owner;
	
	public Municipality(JsonObject municipalityJson) {
    	try {
	    	owner = municipalityJson.get("owner").getAsInt();
	    	JsonObject locationJSON = municipalityJson.getAsJsonObject("location");
	    	VertexDirection dir = VertexDirection.getEnumFromAbbrev(locationJSON.get("direction").getAsString().toUpperCase());
	    	HexLocation hex = new HexLocation(locationJSON.get("x").getAsInt(),locationJSON.get("y").getAsInt());
	    	location = new VertexLocation(hex,dir);
    	}
    	catch (Exception e) {
    		// No resource
    	}
    }
	
	public Municipality(VertexLocation location, int owner) {
		this.location = location;
		this.owner = owner;
	}

	public VertexLocation getLocation() {
		return location.getNormalizedLocation();
	}
	
	public int getOwnerIndex() {
		return owner;
	}
}
