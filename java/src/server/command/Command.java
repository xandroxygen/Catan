package server.command;

import server.facade.IServerFacade;
import shared.model.InvalidActionException;

public abstract class Command {
	
	private int gameID;
	private IServerFacade facade;
	
	public Command(int gameID, IServerFacade facade) {
		this.gameID = gameID;
		this.facade = facade;
	}

	public abstract Object execute() throws InvalidActionException;
	
	public int getGameID() {
		return this.gameID;
	}
	
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}
	
	public IServerFacade getFacade() {
		return this.facade;
	}
	
	public void setFacade(IServerFacade facade) {
		this.facade = facade;
	}
	
	
}
