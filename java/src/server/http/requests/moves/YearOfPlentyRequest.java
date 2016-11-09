package server.http.requests.moves;

import shared.definitions.ResourceType;

/**
 * Helper object for year of plenty requests.
 */
public class YearOfPlentyRequest extends MoveRequest {

	private ResourceType resource1;
	private ResourceType resource2;

	public ResourceType getResource1() {
		return resource1;
	}

	public ResourceType getResource2() {
		return resource2;
	}
}
