package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import server.persistence.Persistence;
import shared.locations.VertexLocation;
import shared.model.InvalidActionException;

public class BuildCityCommand extends Command {
	
	/**
	 * the ID of the player building a city
	 */
	private int playerIndex;
	/**
	 * whether the location that the player wants to build a city is free or not
	 */
	private boolean isFree;
	/**
	 * the location at which the player wants to build a city
	 */
	VertexLocation cityLocation;

	public BuildCityCommand(IServerFacade facade, int gameID, int playerIndex, VertexLocation cityLoc, boolean isFree) {
		super(gameID, facade);
		this.playerIndex = playerIndex;
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
		Object o;
		try {
			o = this.getFacade().buildCity(this.getGameID(), playerIndex, cityLocation);
			Persistence.getInstance().getGameDAO().addCommand(this.getGameID(), this);
			return o;
		}catch(InvalidActionException e){
			throw e;
		}
	}

}
