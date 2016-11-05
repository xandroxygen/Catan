package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.HexLocation;
import shared.model.InvalidActionException;

public class SoldierCommand extends Command {

	private int playerID;
	private HexLocation location;
	private int victimIndex;
	
	public SoldierCommand(IServerFacade facade, int gameID, int playerID, HexLocation location, int victimIndex) {
		super(gameID, facade);
		this.playerID = playerID;
		this.location = location;
		this.victimIndex = victimIndex;
	}

	@Override
	public Object execute() throws InvalidActionException {
		return this.getFacade().playSoldier(this.getGameID(), playerID, location, victimIndex);
	}

}
