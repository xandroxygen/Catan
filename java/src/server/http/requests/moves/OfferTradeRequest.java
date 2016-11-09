package server.http.requests.moves;

import shared.definitions.ResourceType;

import java.util.Map;

/**
 * Helper object of trade offer requests.
 */
public class OfferTradeRequest extends MoveRequest {

	private Map<ResourceType, Integer> offer;
	private int receiver;

	public Map<ResourceType, Integer> getOffer() {
		return offer;
	}

	public int getReceiver() {
		return receiver;
	}
}
