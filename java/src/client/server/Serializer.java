package client.server;

import java.util.Map;

import org.json.simple.JSONObject;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;


public class Serializer {

	/**
	 * Serializes the strings that are passed in. Must be strings (convert ints to strings if necessary).
	 * @param jsonBody the content to be serialized.
	 * @return the serialized JSON string.
	 */
	public static String serialize(Map<String, String> jsonBody) {
		JSONObject jo = new JSONObject();
		for (Map.Entry<String, String> entrySet : jsonBody.entrySet()) {
			jo.put(entrySet.getKey(), entrySet.getValue());
		}
		
		return jo.toJSONString();
	}

	public static String serializeMoveCall(String type, int playerIndex, Map<String, String> jsonBody) {
		JSONObject jo = new JSONObject();
		jo.put("type", type);
		jo.put("playerIndex", playerIndex); // does this need to be toString?
		for (Map.Entry<String, String> entrySet : jsonBody.entrySet()) {
			jo.put(entrySet.getKey(), entrySet.getValue());
		}
		return jo.toJSONString();
	}

	public static String serializeHand(Map<ResourceType, Integer> hand) {
		JSONObject jo = new JSONObject();
		for (Map.Entry<ResourceType, Integer> entrySet : hand.entrySet()) {
			jo.put(entrySet.getKey(), Integer.toString(entrySet.getValue()));
		}
		return jo.toJSONString();
	}

	public static String serializeEdgeLocation(EdgeLocation location) {
		return null;
	}

	public static String serializedVertexLocation(VertexLocation location) {
		return  null;
	}

	public static String serializedHexLocation(HexLocation location) {
		return null;
	}

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
