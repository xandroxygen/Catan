package server.command.moves;

import java.util.Map;

import client.model.InvalidActionException;
import server.command.Command;
import server.facade.IServerFacade;
import shared.definitions.ResourceType;

public class DiscardCardsCommand extends Command {
	
	private Map<ResourceType, Integer> hand;
	
	public DiscardCardsCommand(IServerFacade facade, Map<ResourceType, Integer> hand) {
		super(facade);
		this.hand = hand;
	}

	@Override
	public Object execute() {
		return this.getFacade().discardCards(hand);
	}

}
