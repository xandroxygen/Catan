package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.definitions.ResourceType;

public class YearOfPlentyCommand extends Command {

	private ResourceType resource1;
	private ResourceType resource2;
	
	public YearOfPlentyCommand(IServerFacade facade, ResourceType resource1, ResourceType resource2) {
		super(facade);
		this.resource1 = resource1;
		this.resource2 = resource2;
	}

	@Override
	public Object execute() {
		return this.getFacade().playYearOfPlenty(resource1, resource2);
		
	}

}
