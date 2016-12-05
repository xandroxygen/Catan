package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import server.persistence.Persistence;
import shared.locations.EdgeLocation;
import shared.model.InvalidActionException;

public class RoadBuildingCommand extends Command{
	
	/**
	 * the ID of the player playing the BuildRoad card
	 */
	private int playerIndex;
	/**
	 * the first location to build a road
	 */
	private EdgeLocation location1;
	/**
	 * the second location to build a road
	 */
	private EdgeLocation location2;

	public RoadBuildingCommand(IServerFacade facade, int gameID, int playerIndex, EdgeLocation location1, EdgeLocation location2) {
		super(gameID, facade);
		this.playerIndex = playerIndex;
		this.location1 = location1;
		this.location2 = location2;
	}

	@Override
	/**
	 * Executes the command for playing a Build Road card.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException {
		try {
			Object o = this.getFacade().playRoadBuilding(this.getGameID(), playerIndex, location1, location2);
			Persistence.getInstance().getGameDAO().addCommand(this.getGameID(), this);
			return o;
		}
		catch(InvalidActionException e) {
			throw e;
		}
	}

}
