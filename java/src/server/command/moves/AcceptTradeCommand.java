package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import server.facade.moves.IMovesServerFacade;

public class AcceptTradeCommand extends Command {
	
	private boolean willAccept;

	public AcceptTradeCommand(IServerFacade facade) {
		super(facade);
	}
	
	public AcceptTradeCommand(IServerFacade facade, boolean willAccept) {
		super(facade);
		this.willAccept = willAccept;
	}

	@Override
	public void execute() {
		IMovesServerFacade facade = (IMovesServerFacade)getFacade();
		facade.acceptTrade(willAccept);
		
	}

	public void setWillAccept(boolean willAccept) {
		this.willAccept = willAccept;
	}

}
