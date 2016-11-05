package server.command.moves;

import shared.model.InvalidActionException;
import server.command.Command;
import server.facade.IServerFacade;

public class AcceptTradeCommand extends Command {
	
	private boolean willAccept;

	public AcceptTradeCommand(IServerFacade facade, int gameID, boolean willAccept) {
		super(gameID, facade);
		this.willAccept = willAccept;
	}

	@Override
	public Object execute() throws InvalidActionException {
		return this.getFacade().acceptTrade(this.getGameID(), willAccept);		
	}

}
