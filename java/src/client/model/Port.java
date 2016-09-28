package client.model;

import java.awt.Point;

import shared.definitions.ResourceType;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;

/**
 * Port Class
 */
public class Port {
	private int ratio;
    private ResourceType resource;
    private VertexDirection direction;
    private HexLocation location;
    
    public HexLocation getLocation() {
    	return location;
    }
}
