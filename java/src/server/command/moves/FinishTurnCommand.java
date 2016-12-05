package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import server.persistence.Persistence;
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
		Object o;
		try {
			o = this.getFacade().finishTurn(this.getGameID());
			Persistence.getInstance().getGameDAO().addCommand(this.getGameID(), this);
			return o;
		} catch(InvalidActionException e){
			throw e;
		}
	}

}
