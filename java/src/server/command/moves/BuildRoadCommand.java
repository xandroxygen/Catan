package server.command.moves;

import shared.model.InvalidActionException;
import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.EdgeLocation;

public class BuildRoadCommand extends Command {
	
	/**
	 * the ID of the player building a road
	 */
	private int playerIndex;
	/**
	 * whether the location that the player wants to build a road is free or not
	 */
	private boolean isFree;
	/**
	 * the location at which the player wants to build a road
	 */
	private EdgeLocation roadLocation;

	public BuildRoadCommand(IServerFacade facade, int gameID, int playerIndex, EdgeLocation roadLoc, boolean isFree) {
		super(gameID, facade);
		this.playerIndex = playerIndex;
		this.roadLocation = roadLoc;
		this.isFree = isFree;
	}

	@Override
	/**
	 * Executes the command for building a road.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException {
		return this.getFacade().buildRoad(this.getGameID(), playerIndex, isFree, roadLocation);
	}

	
}
