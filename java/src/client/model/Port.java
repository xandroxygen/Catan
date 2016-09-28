package client.model;

import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;

/**
 * Port Class
 */
public class Port {
	private int ratio;
    private ResourceType resource;
    private EdgeLocation location;

    public ResourceType getResource() {
        return resource;
    }

    public EdgeLocation getLocation() {
    	return location;
    }
}
