package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.model.InvalidActionException;

public class BuyDevCardCommand extends Command {

	/**
	 * the ID of the player buying a Dev card
	 */
	private int playerIndex;
	
	public BuyDevCardCommand(IServerFacade facade, int gameID, int playerIndex) {
		super(gameID, facade);
		this.playerIndex = playerIndex;
	}

	@Override
	/**
	 * Executes the command for buying a Dev card.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException {
		return this.getFacade().buyDevCard(this.getGameID(), playerIndex);		
	}
}
