package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.InvalidActionException;

public class BuildCityCommand extends Command {
	
	private int playerID;
	boolean isFree;
	VertexLocation vertexLoc;

	public BuildCityCommand(IServerFacade facade, int gameID, int playerID, VertexLocation vertexLoc, boolean isFree) {
		super(gameID, facade);
		this.playerID = playerID;
		this.vertexLoc = vertexLoc;
		this.isFree = isFree;
	}

	@Override
	public Object execute() throws InvalidActionException {
		return this.getFacade().buildCity(this.getGameID(), playerID, vertexLoc);	
	}

}
