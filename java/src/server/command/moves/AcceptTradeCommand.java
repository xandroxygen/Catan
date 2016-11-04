package server.command.moves;

import client.model.InvalidActionException;
import server.command.Command;
import server.facade.IServerFacade;

public class AcceptTradeCommand extends Command {
	
	private boolean willAccept;

	public AcceptTradeCommand(IServerFacade facade, boolean willAccept) {
		super(facade);
		this.willAccept = willAccept;
	}

	@Override
	public Object execute() {
		return this.getFacade().acceptTrade(willAccept);		
	}

}
