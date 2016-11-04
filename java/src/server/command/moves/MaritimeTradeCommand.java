package server.command.moves;

import client.model.InvalidActionException;
import server.command.Command;
import server.facade.IServerFacade;
import shared.definitions.ResourceType;

public class MaritimeTradeCommand extends Command {
	
	private int ratio;
	private ResourceType inputResource;
	private ResourceType outputResource;

	public MaritimeTradeCommand(IServerFacade facade, int ratio, ResourceType inputResource, ResourceType outputResource) {
		super(facade);
		this.ratio = ratio;
		this.inputResource = inputResource;
		this.outputResource = outputResource;
	}

	@Override
	public Object execute() {
		return this.getFacade().maritimeTrade(ratio, inputResource, outputResource);		
	}

}
