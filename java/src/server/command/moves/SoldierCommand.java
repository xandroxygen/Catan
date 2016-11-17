package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.HexLocation;
import shared.model.InvalidActionException;

public class SoldierCommand extends Command {

	/**
	 * the ID of the player playing the Soldier card
	 */
	private int playerIndex;
	/**
	 * the location where the Soldier card will be played
	 */
	private HexLocation location;
	/**
	 * the index of the person that the Soldier card is being played against
	 */
	private int victimIndex;
	
	public SoldierCommand(IServerFacade facade, int gameID, int playerIndex, HexLocation location, int victimIndex) {
		super(gameID, facade);
		this.playerIndex = playerIndex;
		this.location = location;
		this.victimIndex = victimIndex;
	}

	@Override
	/**
	 * Executes the command for playing the Soldier card.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException {
		return this.getFacade().playSoldier(this.getGameID(), playerIndex, location, victimIndex);
	}

}
