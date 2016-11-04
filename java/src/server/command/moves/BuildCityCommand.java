package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.EdgeLocation;

public class BuildCityCommand extends Command {
	boolean isFree;
	EdgeLocation roadLoc;

	public BuildCityCommand(IServerFacade facade, EdgeLocation roadLocation, boolean isFree) {
		super(facade);
		this.roadLoc = roadLocation;
		this.isFree = isFree;
	}

	@Override
	public Object execute() {
		return this.getFacade().buildCity(roadLoc);
		
	}

}
