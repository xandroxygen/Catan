package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.model.InvalidActionException;

public class BuyDevCardCommand extends Command {

	/**
	 * the ID of the player buying a Dev card
	 */
	private int playerID;
	
	public BuyDevCardCommand(IServerFacade facade, int gameID, int playerID) {
		super(gameID, facade);
		this.playerID = playerID;
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
		return this.getFacade().buyDevCard(this.getGameID(), playerID);		
	}
}
