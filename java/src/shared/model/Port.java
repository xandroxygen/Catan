package shared.model;

import com.google.gson.JsonObject;

import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;

/**
 * Port Class
 */
public class Port {
	private int ratio;
    private ResourceType resource;
    private EdgeLocation location;
    
    public Port(int ratio, ResourceType type) {
    	this.ratio = ratio;
    	this.resource = type;
    	location = null;
    }
    
    public void setLocation(EdgeLocation location) {
    	this.location = location;
    }
    
    public Port(JsonObject portJson) {
    	try {
	    	ratio = portJson.get("ratio").getAsInt();
	    	EdgeDirection dir = EdgeDirection.getEnumFromAbbrev(portJson.get("direction").getAsString().toUpperCase());
	    	JsonObject locationJSON = portJson.getAsJsonObject("location");
	    	HexLocation hex = new HexLocation(locationJSON.get("x").getAsInt(),locationJSON.get("y").getAsInt());
	    	location = new EdgeLocation(hex,dir);
	    	resource = ResourceType.valueOf(portJson.get("resource").getAsString().toUpperCase());
    	}
    	catch (Exception e) {
    		// No resource
    	}
    }

    public ResourceType getResource() {
        return resource;
    }

    public EdgeLocation getLocation() {
    	return location;
    }
    
    public int getRatio() {
    	return ratio;
    }
}
