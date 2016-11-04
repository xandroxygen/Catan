package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.EdgeLocation;

public class RoadBuildingCommand extends Command{
	
	private EdgeLocation location1;
	private EdgeLocation location2;

	public RoadBuildingCommand(IServerFacade facade, EdgeLocation location1, EdgeLocation location2) {
		super(facade);
		this.location1 = location1;
		this.location2 = location2;
	}

	@Override
	public Object execute() {
		return this.getFacade().playRoadBuilding(location1, location2);
		
	}

}
