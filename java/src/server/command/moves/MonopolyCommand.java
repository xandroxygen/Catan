package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.definitions.ResourceType;

public class MonopolyCommand extends Command{
	
	private ResourceType resource;
	
	public MonopolyCommand(IServerFacade facade, ResourceType resource) {
		super(facade);
		this.resource = resource;
	}

	@Override
	public Object execute() {
		return this.getFacade().playMonopoly(resource);
		
	}

}
