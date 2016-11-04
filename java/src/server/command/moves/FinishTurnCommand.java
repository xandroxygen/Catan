package server.command.moves;

import client.model.InvalidActionException;
import server.command.Command;
import server.facade.IServerFacade;

public class FinishTurnCommand extends Command{

	public FinishTurnCommand(IServerFacade facade) {
		super(facade);
	}

	@Override
	public Object execute() {
		return this.getFacade().finishTurn();
		
	}

}
