package server.command.moves;

import java.util.Map;

import client.model.InvalidActionException;
import server.command.Command;
import server.facade.IServerFacade;
import shared.definitions.ResourceType;

public class DiscardCardsCommand extends Command {
	
	private int playerID;
	private Map<ResourceType, Integer> hand;
	
	public DiscardCardsCommand(IServerFacade facade, int gameID, int playerID, Map<ResourceType, Integer> hand) {
		super(gameID, facade);
		this.playerID = playerID;
		this.hand = hand;
	}

	@Override
	public Object execute() {
		return this.getFacade().discardCards(this.getGameID(), playerID, hand);
	}

}
