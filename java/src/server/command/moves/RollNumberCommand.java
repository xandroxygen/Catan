package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.model.InvalidActionException;

public class RollNumberCommand extends Command {
	
	/**
	 * the ID of the player that rolled
	 */
	private int playerID;
	/**
	 * the value that was rolled
	 */
	private int rollValue;

	public RollNumberCommand(IServerFacade facade, int gameID, int playerID, int rollValue) {
		super(gameID, facade);
		this.playerID = playerID;
		this.rollValue = rollValue;
	}

	@Override
	/**
	 * Executes the command for rolling the dice.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException {
		return this.getFacade().rollNumber(this.getGameID(), playerID, rollValue);
	}

}
