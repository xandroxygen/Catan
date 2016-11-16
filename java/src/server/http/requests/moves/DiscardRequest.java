package server.http.requests.moves;

import shared.definitions.ResourceType;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper object for discarding cards requests.
 */
public class DiscardRequest extends MoveRequest {

	private HashMap<ResourceType, Integer> discardedCards;

	public HashMap<ResourceType, Integer> getDiscardedCards() {
		return discardedCards;
	}
}
