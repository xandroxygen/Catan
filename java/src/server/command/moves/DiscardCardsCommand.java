package server.command.moves;

import java.util.Map;

import server.command.Command;
import server.facade.IServerFacade;
import server.persistence.Persistence;
import shared.definitions.ResourceType;
import shared.model.InvalidActionException;

public class DiscardCardsCommand extends Command {
	
	/**
	 * the ID of the player discarding his cards
	 */
	private int playerIndex;
	/**
	 * the cards being discarded
	 */
	private Map<ResourceType, Integer> hand;
	
	public DiscardCardsCommand(IServerFacade facade, int gameID, int playerIndex, Map<ResourceType, Integer> hand) {
		super(gameID, facade);
		this.playerIndex = playerIndex;
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
		Object o;
		try {
			o = this.getFacade().discardCards(this.getGameID(), playerIndex, hand);
		} catch(InvalidActionException e){
			throw e;
		}
		Persistence.getInstance().getGameDAO().addCommand(this.getGameID(), this);
		return o;
	}

}
