package server.command.moves;

import client.model.InvalidActionException;
import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.VertexLocation;

public class BuildSettlementCommand extends Command {

	private boolean isFree;
	private VertexLocation vertexLoc;
	
	public BuildSettlementCommand(IServerFacade facade, VertexLocation vertexLoc, boolean isFree) {
		super(facade);
		this.vertexLoc = vertexLoc;
		this.isFree = isFree;
	}

	@Override
	public Object execute(){
		return this.getFacade().buildSettlement(isFree, vertexLoc);
	}

}
