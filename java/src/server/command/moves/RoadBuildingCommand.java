package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.EdgeLocation;
import shared.model.InvalidActionException;

public class RoadBuildingCommand extends Command{
	
	private int playerID;
	private EdgeLocation location1;
	private EdgeLocation location2;

	public RoadBuildingCommand(IServerFacade facade, int gameID, int playerID, EdgeLocation location1, EdgeLocation location2) {
		super(gameID, facade);
		this.playerID = playerID;
		this.location1 = location1;
		this.location2 = location2;
	}

	@Override
	public Object execute() throws InvalidActionException {
		return this.getFacade().playRoadBuilding(this.getGameID(), playerID, location1, location2);
	}

}
