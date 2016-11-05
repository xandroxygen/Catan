package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.model.InvalidActionException;

public class MonumentCommand extends Command {

	private int playerID; 
	
	public MonumentCommand(IServerFacade facade, int gameID, int playerID) {
		super(gameID, facade);
		this.playerID = playerID;
	}

	@Override
	public Object execute() throws InvalidActionException {
		return this.getFacade().playMonument(this.getGameID(), playerID);
	}

}
