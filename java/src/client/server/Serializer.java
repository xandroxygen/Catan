package client.server;

import java.util.Map;

import org.json.simple.JSONObject;
import shared.definitions.ResourceType;
import shared.locations.*;

/**
 * Serializes the information necessary for making API calls to the server. There are generic methods for producing a
 * normal JSON string, and more specific methods for move calls, classes located in shared.locations, and others.
 * These are static, so call without creating a Serializer object.
 */
public class Serializer {

	/**
	 * Serializes the strings that are passed in. Must be strings (convert ints to strings if necessary).
	 * This is a generic method that returns a generic JSON string.
	 * @param jsonBody the content to be serialized.
	 * @return the serialized JSON string.
	 */
	public static String serializeNonMoveCall(Map<String, String> jsonBody) {
		JSONObject jo = new JSONObject();
		for (Map.Entry<String, String> entrySet : jsonBody.entrySet()) {
			jo.put(entrySet.getKey(), entrySet.getValue());
		}
		
		return jo.toJSONString();
	}

	/**
	 * Specialized serialization method for move API calls. This is also fairly generic in that it returns a JSON string
	 * with anything that gets passed in, and in addition a type and playerIndex (which must be an int).
	 * @param type Type of the API call, which is usually the same as the URL (eg, sendChat)
	 * @param playerIndex NOT playerID, but index for turns (0-3)
	 * @param jsonBody More strings that need to be passed in.
	 * @return a JSON string
	 */
	public static String serializeMoveCall(String type, int playerIndex, Map<String, String> jsonBody) {
		JSONObject jo = new JSONObject();
		jo.put("type", type);
		jo.put("playerIndex", playerIndex); // TODO: does this need to be toString?
		for (Map.Entry<String, String> entrySet : jsonBody.entrySet()) {
			jo.put(entrySet.getKey(), entrySet.getValue());
		}
		return jo.toJSONString();
	}

	/**
	 * Takes in a hand or deck of cards, and serializes into a JSON string to be included in API calls.
	 * This currently defines a hand as a map of ResourceType to integer, but soon will include a Hand class.
	 * @param hand the hand or deck of cards to be serialized.
	 * @return the JSON representation of the hand of cards
	 */
	public static String serializeHand(Map<ResourceType, Integer> hand) {
		JSONObject jo = new JSONObject();
		for (Map.Entry<ResourceType, Integer> entrySet : hand.entrySet()) {
			jo.put(serializeResourceType(entrySet.getKey()), Integer.toString(entrySet.getValue()));
		}
		return jo.toJSONString();
	}

	/**
	 * Helper function to get JSON string representing a HexLocation
	 * @param location the HexLocation to serialize
	 * @return the JSON representation of the HexLocation
	 */
	public static String serializeHexLocation(HexLocation location) {
		JSONObject jo = new JSONObject();
		jo.put("x", location.getX());
		jo.put("y", location.getY());
		return jo.toJSONString();
	}

	/**
	 * Helper function to get JSON string representing an EdgeLocation
	 * @param location the HexLocation to serialize
	 * @return the JSON representation of the EdgeLocation
	 */
	public static String serializeEdgeLocation(EdgeLocation location) {
		JSONObject jo = new JSONObject();
		jo.put("x", location.getHexLoc().getX());
		jo.put("y", location.getHexLoc().getY());
		jo.put("direction", serializeEdgeDirection(location.getDir()));
		return jo.toJSONString();
	}

	/**
	 * Helper function to get JSON string representing a VertexLocation
	 * @param location the HexLocation to serialize
	 * @return the JSON representation of the VertexLocation
	 */
	public static String serializeVertexLocation(VertexLocation location) {
		JSONObject jo = new JSONObject();
		jo.put("x", location.getHexLoc().getX());
		jo.put("y", location.getHexLoc().getY());
		jo.put("direction", serializeVertexDirection(location.getDir()));
		return jo.toJSONString();
	}

	/**
	 * Helper function to get string representation of a VertexDirection.
	 * Note: NOT JSON, but a normal string.
	 * @param direction the direction to serialize
	 * @return the String representation of a VertexDirection
	 */
	public static String serializeVertexDirection(VertexDirection direction) {
		String ret = "";
		switch (direction) {
			case NorthEast:
				ret = "NE";
				break;
			case NorthWest:
				ret = "NW";
				break;
			case East:
				ret = "E";
				break;
			case West:
				ret = "W";
				break;
			case SouthEast:
				ret = "SE";
				break;
			case SouthWest:
				ret = "SW";
				break;
		}
		return ret;
	}

	/**
	 * Helper function to get string representation of a EdgeDirection.
	 * Note: NOT JSON, but a normal string.
	 * @param direction the direction to serialize
	 * @return the String representation of a EdgeDirection
	 */
	public static String serializeEdgeDirection(EdgeDirection direction) {
		String ret = "";
		switch (direction) {
			case NorthEast:
				ret = "NE";
				break;
			case NorthWest:
				ret = "NW";
				break;
			case North:
				ret = "N";
				break;
			case South:
				ret = "S";
				break;
			case SouthEast:
				ret = "SE";
				break;
			case SouthWest:
				ret = "SW";
				break;
		}
		return ret;
	}

	/**
	 * Helper function to get string representation of a ResourceType.
	 * Note: NOT JSON, but a normal string.
	 * @param resourceType the direction to serialize
	 * @return the String representation of a ResourceType
	 */
	public static String serializeResourceType(ResourceType resourceType) {
		String ret = "";
		switch (resourceType) {
			case WOOD:
				ret = "wood";
				break;
			case BRICK:
				ret = "brick";
				break;
			case WHEAT:
				ret = "wheat";
				break;
			case SHEEP:
				ret = "sheep";
				break;
			case ORE:
				ret = "ore";
				break;
		}
		return ret;
	}
}
