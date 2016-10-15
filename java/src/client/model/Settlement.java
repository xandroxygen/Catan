package client.model;

import com.google.gson.JsonObject;

import shared.locations.VertexLocation;

/**
 *Settlement Class
 */
public class Settlement extends Municipality {
	public Settlement(VertexLocation location, int owner) {
		super(location, owner);
	}
	
	public Settlement(JsonObject settlementJson) {
		super(settlementJson);
	}
}
