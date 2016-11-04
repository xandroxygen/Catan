package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.HexLocation;

public class SoldierCommand extends Command {

	private HexLocation location;
	private int victimIndex;
	
	public SoldierCommand(IServerFacade facade, HexLocation location, int victimIndex) {
		super(facade);
		this.location = location;
		this.victimIndex = victimIndex;
	}

	@Override
	public Object execute() {
		return this.getFacade().playSoldier(location, victimIndex);
		
	}

}
