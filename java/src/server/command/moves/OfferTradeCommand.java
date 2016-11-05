package server.command.moves;

import java.util.Map;

import server.command.Command;
import server.facade.IServerFacade;
import shared.definitions.ResourceType;
import shared.model.InvalidActionException;

public class OfferTradeCommand extends Command{
	
	private int senderID;
	private Map<ResourceType, Integer> offer;
	private int receiverID;
	
	public OfferTradeCommand(IServerFacade facade, int gameID, int senderID, int receiverID, Map<ResourceType, Integer> offer) {
		super(gameID, facade);
		this.senderID = senderID;
		this.receiverID = receiverID;
		this.offer = offer;
	}

	@Override
	public Object execute() throws InvalidActionException {
		return this.getFacade().offerTrade(this.getGameID(), senderID, receiverID, offer);
	}

}
