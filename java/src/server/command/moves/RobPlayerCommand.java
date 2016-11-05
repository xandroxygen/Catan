package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.HexLocation;
import shared.model.InvalidActionException;

public class RobPlayerCommand extends Command{

	private int playerID;
	private HexLocation location;
	private int victimIndex;
	
	public RobPlayerCommand(IServerFacade facade, int gameID, int playerID, int victimIndex, HexLocation location) {
		super(gameID, facade);
		this.location = location;
		this.victimIndex = victimIndex;
	}

	@Override
	public Object execute() throws InvalidActionException{
		//return this.getFacade().robPlayer(this.getGameID(), playerID, victimIndex, location);
		return null;
	}

}
