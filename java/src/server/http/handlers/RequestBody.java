package server.http.handlers;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper object for the parameters sent in the HTTP Request.
 * Contains attributes, which is a Map of Strings to Objects.
 * Call get('attribute-name') to get the Object, and cast it to its correct class.
 * Specifications for which parameters come with which request is located in the Swagger API.
 */
public class RequestBody {

	private Map<String, Object> attributes;

	public RequestBody() {
		attributes = new HashMap<>();
	}

	public void add(String key, Object value) {
		attributes.put(key, value);
	}

	public Object get(String key) {
		return attributes.get(key);
	}
}
