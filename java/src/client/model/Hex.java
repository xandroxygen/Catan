package client.model;

import java.awt.Point;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import shared.definitions.ResourceType;
import shared.locations.HexLocation;

/**
 * Hex class.
 */
public class Hex {
	private ResourceType resource;
	private HexLocation location;
	private int number;
	
	public HexLocation getLocation() {
		return location;
	}
	
}



