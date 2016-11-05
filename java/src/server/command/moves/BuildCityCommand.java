package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.InvalidActionException;

public class BuildCityCommand extends Command {
	
	/**
	 * the ID of the player building a city
	 */
	private int playerID;
	/**
	 * whether the location that the player wants to build a city is free or not
	 */
	private boolean isFree;
	/**
	 * the location at which the player wants to build a city
	 */
	VertexLocation cityLocation;

	public BuildCityCommand(IServerFacade facade, int gameID, int playerID, VertexLocation cityLoc, boolean isFree) {
		super(gameID, facade);
		this.playerID = playerID;
		this.cityLocation = cityLoc;
		this.isFree = isFree;
	}

	@Override
	/**
	 * Executes the command for building a city.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException {
		return this.getFacade().buildCity(this.getGameID(), playerID, cityLocation);	
	}

}
