package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.model.InvalidActionException;

public class RollNumberCommand extends Command {
	
	/**
	 * the ID of the player that rolled
	 */
	private int playerIndex;
	/**
	 * the value that was rolled
	 */
	private int rollValue;

	public RollNumberCommand(IServerFacade facade, int gameID, int playerIndex, int rollValue) {
		super(gameID, facade);
		this.playerIndex = playerIndex;
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
		return this.getFacade().rollNumber(this.getGameID(), playerIndex, rollValue);
	}

}
