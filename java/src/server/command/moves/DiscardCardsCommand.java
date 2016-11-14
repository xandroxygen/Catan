package server.command.moves;

import java.util.HashMap;
import java.util.Map;

import server.command.Command;
import server.facade.IServerFacade;
import shared.definitions.ResourceType;
import shared.model.InvalidActionException;

public class DiscardCardsCommand extends Command {
	
	/**
	 * the ID of the player discarding his cards
	 */
	private int playerID;
	/**
	 * the cards being discarded
	 */
	private HashMap<ResourceType, Integer> hand;
	
	public DiscardCardsCommand(IServerFacade facade, int gameID, int playerID, HashMap<ResourceType, Integer> hand) {
		super(gameID, facade);
		this.playerID = playerID;
		this.hand = hand;
	}

	@Override
	/**
	 * Executes the command for discarding a player's cards.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException {
		return this.getFacade().discardCards(this.getGameID(), playerID, hand);
	}

}
