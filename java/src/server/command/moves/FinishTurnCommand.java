package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.model.InvalidActionException;

public class FinishTurnCommand extends Command{

	public FinishTurnCommand(int gameID, IServerFacade facade) {
		super(gameID, facade);
	}

	@Override
	/**
	 * Executes the command for finishing a player's turn.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException {
		return this.getFacade().finishTurn(this.getGameID());
	}

}
