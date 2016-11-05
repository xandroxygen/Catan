package server.command.moves;

import shared.model.InvalidActionException;
import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.VertexLocation;

public class BuildSettlementCommand extends Command {

	private int playerID;
	private boolean isFree;
	private VertexLocation vertexLoc;
	
	public BuildSettlementCommand(IServerFacade facade, int gameID, int playerID, VertexLocation vertexLoc, boolean isFree) {
		super(gameID, facade);
		this.playerID = playerID;
		this.vertexLoc = vertexLoc;
		this.isFree = isFree;
	}

	@Override
	public Object execute() throws InvalidActionException{
		return this.getFacade().buildSettlement(this.getGameID(), playerID, isFree, vertexLoc);
	}

}
