package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.HexLocation;
import shared.model.InvalidActionException;

public class RobPlayerCommand extends Command{

	/**
	 * the Index of the player doing the robbing
	 */
	private int playerIndex;
	/**
	 * the location to place the robber
	 */
	private HexLocation location;
	/**
	 * the index of the player being robbed
	 */
	private int victimIndex;
	
	public RobPlayerCommand(IServerFacade facade, int gameID, int playerIndex, int victimIndex, HexLocation location) {
		super(gameID, facade);
		this.playerIndex = playerIndex;
		this.location = location;
		this.victimIndex = victimIndex;
	}

	@Override
	/**
	 * Executes the command for robbing a player.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException{
		return this.getFacade().robPlayer(this.getGameID(), playerIndex, location, victimIndex);
	}

}
