package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.definitions.ResourceType;
import shared.model.InvalidActionException;

public class MaritimeTradeCommand extends Command {
	
	private int playerID;
	private int ratio;
	private ResourceType inputResource;
	private ResourceType outputResource;

	public MaritimeTradeCommand(IServerFacade facade, int gameID, int playerID, int ratio, ResourceType inputResource, ResourceType outputResource) {
		super(gameID, facade);
		this.playerID = playerID;
		this.ratio = ratio;
		this.inputResource = inputResource;
		this.outputResource = outputResource;
	}

	@Override
	public Object execute() throws InvalidActionException{
		//return this.getFacade().maritimeTrade(this.getGameID(), playerID, ratio, inputResource, outputResource);		
		return null;
	}

}
