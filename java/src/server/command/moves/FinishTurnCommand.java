package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.model.InvalidActionException;

public class FinishTurnCommand extends Command{

	public FinishTurnCommand(int gameID, IServerFacade facade) {
		super(gameID, facade);
	}

	@Override
	public Object execute() throws InvalidActionException {
		return this.getFacade().finishTurn(this.getGameID());
	}

}
