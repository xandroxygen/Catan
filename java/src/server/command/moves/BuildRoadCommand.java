package server.command.moves;

import client.model.InvalidActionException;
import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.EdgeLocation;

public class BuildRoadCommand extends Command {
	
	private boolean isFree;
	private EdgeLocation roadLocation;

	public BuildRoadCommand(IServerFacade facade, EdgeLocation roadLocation, boolean isFree) {
		super(facade);
		this.roadLocation = roadLocation;
		this.isFree = isFree;
	}

	@Override
	public Object execute() {
		return this.getFacade().buildRoad(isFree, roadLocation);
	}

	
}
