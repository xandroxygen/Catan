package server.http.requests.moves;

import shared.definitions.ResourceType;
import shared.model.InvalidActionException;

/**
 * Base class for all move requests.
 */
public class MoveRequest {

	private String type;
	private int playerIndex;

	public String getType() {
		return type;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public ResourceType findResource(String resource) throws InvalidActionException {
		for (ResourceType r : ResourceType.values()) {
			if (r.toString().equals(resource)) {
				return r;
			}
		}
		throw new InvalidActionException("Invalid request - improper resource type");
	}
}
