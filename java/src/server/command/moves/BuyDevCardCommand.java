package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.model.InvalidActionException;

public class BuyDevCardCommand extends Command {

	private int playerID;
	
	public BuyDevCardCommand(IServerFacade facade, int gameID, int playerID) {
		super(gameID, facade);
		this.playerID = playerID;
	}

	@Override
	public Object execute() throws InvalidActionException {
		return this.getFacade().buyDevCard(this.getGameID(), playerID);		
	}
}
