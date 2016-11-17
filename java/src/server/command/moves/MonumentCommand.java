package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.model.InvalidActionException;

public class MonumentCommand extends Command {
	
	/**
	 * the ID of the player playing the Monument card
	 */
	private int playerIndex; 
	
	public MonumentCommand(IServerFacade facade, int gameID, int playerIndex) {
		super(gameID, facade);
		this.playerIndex = playerIndex;
	}

	@Override
	/**
	 * Executes the command for playing a monument card.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException {
		return this.getFacade().playMonument(this.getGameID(), playerIndex);
	}

}
