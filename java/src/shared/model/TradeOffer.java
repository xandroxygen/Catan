package shared.model;
import com.google.gson.JsonObject;
import shared.definitions.ResourceType;

import java.util.*;
import java.util.Map;

/**
 * Trade Offer Class
 */
public class TradeOffer {

	private int sender;
	private int receiver;
	private Map<ResourceType, Integer> offer;

	public TradeOffer(int senderPlayerID, int receiverPlayerID, Map<ResourceType, Integer> offer){
		sender = senderPlayerID;
		receiver = receiverPlayerID;
		this.offer = offer;
	}

	public TradeOffer(JsonObject tradeJSON) {

		sender = tradeJSON.get("sender").getAsInt();
		receiver = tradeJSON.get("receiver").getAsInt();

		// parse offer
		offer = new HashMap<>();
		JsonObject offerJSON = tradeJSON.getAsJsonObject("offer");
		offer.put(ResourceType.BRICK, offerJSON.get("brick").getAsInt());
		offer.put(ResourceType.WOOD, offerJSON.get("wood").getAsInt());
		offer.put(ResourceType.WHEAT, offerJSON.get("wheat").getAsInt());
		offer.put(ResourceType.SHEEP, offerJSON.get("sheep").getAsInt());
		offer.put(ResourceType.ORE, offerJSON.get("ore").getAsInt());

	}

	public int getSender() {
		return sender;
	}

	public void setSender(int sender) {
		this.sender = sender;
	}

	public int getReceiver() {
		return receiver;
	}

	public void setReceiver(int receiver) {
		this.receiver = receiver;
	}

	public Map<ResourceType, Integer> getOffer() {
		return offer;
	}

	public void setOffer(HashMap<ResourceType, Integer> offer) {
		this.offer = offer;
	}

	
}
