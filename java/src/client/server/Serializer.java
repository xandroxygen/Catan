package client.server;

import java.util.Map;

import org.json.simple.JSONObject;


public class Serializer {

	public static String serialize(Map<String, String> jsonBody) {
		JSONObject jo = new JSONObject();
		for (Map.Entry<String, String> entrySet : jsonBody.entrySet()) {
			jo.put(entrySet.getKey(), entrySet.getValue());
		}
		
		return jo.toJSONString();
	}
}
