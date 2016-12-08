package shared.model;

import java.io.Serializable;

import com.google.gson.JsonObject;

import shared.locations.VertexLocation;

/**
 * City class
 */
public class City extends Municipality implements Serializable {
	
	public City(VertexLocation location, int owner) {
		super(location, owner);
	}
	
	public City(JsonObject cityJson) {
		super(cityJson);
	}

}
