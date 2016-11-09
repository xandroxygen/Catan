package server.http.requests.moves;

import shared.definitions.ResourceType;

import java.util.Map;

/**
 * Helper object for discarding cards requests.
 */
public class DiscardRequest extends MoveRequest {

	private Map<ResourceType, Integer> discardedCards;

	public Map<ResourceType, Integer> getDiscardedCards() {
		return discardedCards;
	}
}
