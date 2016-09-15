package client.model;
import java.util.HashMap;

import shared.definitions.*;

public class TradeOffer {
	private int sender;
	private int receiver;
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
	public HashMap<ResourceType, Integer> getOffer() {
		return offer;
	}
	public void setOffer(HashMap<ResourceType, Integer> offer) {
		this.offer = offer;
	}
	private HashMap<ResourceType, Integer> offer;
	
	
}
