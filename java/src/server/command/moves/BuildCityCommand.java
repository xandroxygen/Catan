package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.EdgeLocation;

public class BuildCityCommand extends Command {
	boolean isFree;
	EdgeLocation roadLocation;

	public BuildCityCommand(IServerFacade facade, EdgeLocation roadLocation, boolean isFree) {
		super(facade);
		this.roadLocation = roadLocation;
		this.isFree = isFree;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

}
