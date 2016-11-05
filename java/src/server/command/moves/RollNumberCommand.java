package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.model.InvalidActionException;

public class RollNumberCommand extends Command {
	
	private int playerID;
	private int rollValue;

	public RollNumberCommand(IServerFacade facade, int gameID, int playerID, int rollValue) {
		super(gameID, facade);
		this.playerID = playerID;
		this.rollValue = rollValue;
	}

	@Override
	public Object execute() throws InvalidActionException {
		return this.getFacade().rollNumber(this.getGameID(), playerID, rollValue);
	}

}
