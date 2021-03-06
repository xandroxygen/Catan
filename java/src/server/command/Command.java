package server.command;

import server.facade.IServerFacade;
import shared.model.InvalidActionException;

import java.io.Serializable;

public abstract class Command implements Serializable {
	
	/**
	 * the ID of the game on which to perform the command
	 */
	private int gameID;
	
	/**
	 * the IServerFacade with which to interact
	 */
	transient private IServerFacade facade;
	
	public Command(int gameID, IServerFacade facade) {
		this.gameID = gameID;
		this.facade = facade;
	}

	/**
	 * This is called when the command is to be executed
	 * 
	 * @return the result of the execution of the command
	 * @throws InvalidActionException
	 */
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
