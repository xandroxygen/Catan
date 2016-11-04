package server.command;

import client.model.InvalidActionException;
import server.facade.IServerFacade;

public abstract class Command {
	
	private IServerFacade facade;
	
	public Command(IServerFacade facade) {
		this.facade = facade;
	}

	public abstract Object execute();
	
	public IServerFacade getFacade() {
		return this.facade;
	}
	
	public void setFacade(IServerFacade facade) {
		this.facade = facade;
	}
	
	
}
