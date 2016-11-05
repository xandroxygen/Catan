package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.definitions.ResourceType;
import shared.model.InvalidActionException;

public class MonopolyCommand extends Command{
	
	private int playerID;
	private ResourceType resource;
	
	public MonopolyCommand(IServerFacade facade, int gameID, int playerID, ResourceType resource) {
		super(gameID, facade);
		this.playerID = playerID;
		this.resource = resource;
	}

	@Override
	public Object execute() throws InvalidActionException {
		return this.getFacade().playMonopoly(this.getGameID(), playerID, resource);
	}

}
