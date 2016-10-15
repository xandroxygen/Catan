package client.model;

import com.google.gson.JsonObject;

import shared.locations.VertexLocation;

/**
 * City class
 */
public class City extends Municipality {
	
	public City(VertexLocation location, int owner) {
		super(location, owner);
	}
	
	public City(JsonObject cityJson) {
		super(cityJson);
	}

}
