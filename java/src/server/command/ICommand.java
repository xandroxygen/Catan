package server.command;

import server.facade.IServerFacade;

public abstract class ICommand {
	
	private IServerFacade facade;
	
	public ICommand(IServerFacade facade) {
		this.facade = facade;
	}

	public abstract void execute();
	
	
}
