package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.HexLocation;

public class RobPlayerCommand extends Command{

	private HexLocation location;
	private int victimIndex;
	
	public RobPlayerCommand(IServerFacade facade, HexLocation location, int victimIndex) {
		super(facade);
		this.location = location;
		this.victimIndex = victimIndex;
	}

	@Override
	public Object execute() {
		return this.getFacade().robPlayer(location, victimIndex);
	}

}
