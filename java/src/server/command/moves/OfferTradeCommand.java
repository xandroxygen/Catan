package server.command.moves;

import java.util.Map;

import server.command.Command;
import server.facade.IServerFacade;
import shared.definitions.ResourceType;

public class OfferTradeCommand extends Command{
	
	private Map<ResourceType, Integer> offer;
	private int receiverIndex;
	
	public OfferTradeCommand(IServerFacade facade, Map<ResourceType, Integer> offer, int receiverIndex) {
		super(facade);
		this.offer = offer;
		this.receiverIndex = receiverIndex;
	}

	@Override
	public Object execute() {
		return this.getFacade().offerTrade(offer, receiverIndex);
		
	}

}
