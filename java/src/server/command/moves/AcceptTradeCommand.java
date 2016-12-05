package server.command.moves;

import server.persistence.Persistence;
import shared.model.InvalidActionException;
import server.command.Command;
import server.facade.IServerFacade;

public class AcceptTradeCommand extends Command {
	
	/**
	 * whether the player being offered a trade will accept or not
	 */
	private boolean willAccept;

	public AcceptTradeCommand(IServerFacade facade, int gameID, boolean willAccept) {
		super(gameID, facade);
		this.willAccept = willAccept;
	}

	@Override
	/**
	 * Executes the command for accepting/ rejecting a trade.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException {
		Object o;
		try {
			o = this.getFacade().acceptTrade(this.getGameID(), willAccept);
			Persistence.getInstance().getGameDAO().addCommand(this.getGameID(), this);
			return o;
		}catch(InvalidActionException e){
			throw e;
		}
	}

}
