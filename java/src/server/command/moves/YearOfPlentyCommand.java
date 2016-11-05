package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.definitions.ResourceType;
import shared.model.InvalidActionException;

public class YearOfPlentyCommand extends Command {

	private int playerID;
	private ResourceType resource1;
	private ResourceType resource2;
	
	public YearOfPlentyCommand(IServerFacade facade, int gameID, int playerID, ResourceType resource1, ResourceType resource2) {
		super(gameID, facade);
		this.playerID = playerID;
		this.resource1 = resource1;
		this.resource2 = resource2;
	}

	@Override
	public Object execute() throws InvalidActionException {
		return this.getFacade().playYearOfPlenty(this.getGameID(), playerID, resource1, resource2);	
	}

}
