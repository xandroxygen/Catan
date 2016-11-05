package server.command.moves;

import shared.model.InvalidActionException;
import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.EdgeLocation;

public class BuildRoadCommand extends Command {
	
	private int playerID;
	private boolean isFree;
	private EdgeLocation roadLocation;

	public BuildRoadCommand(IServerFacade facade, int gameID, int playerID, EdgeLocation roadLocation, boolean isFree) {
		super(gameID, facade);
		this.playerID = playerID;
		this.roadLocation = roadLocation;
		this.isFree = isFree;
	}

	@Override
	public Object execute() throws InvalidActionException {
		return this.getFacade().buildRoad(this.getGameID(), playerID, isFree, roadLocation);
	}

	
}
