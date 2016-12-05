package server.command.moves;

import server.persistence.Persistence;
import shared.model.InvalidActionException;
import server.command.Command;
import server.facade.IServerFacade;
import shared.locations.VertexLocation;

public class BuildSettlementCommand extends Command {

	/**
	 * the ID of the player building a settlement
	 */
	private int playerIndex;
	/**
	 * whether the location that the player wants to build a settlement is free or not
	 */
	private boolean isFree;
	/**
	 * the location at which the player wants to build a settlement
	 */
	private VertexLocation settlementLocation;
	
	public BuildSettlementCommand(IServerFacade facade, int gameID, int playerIndex, VertexLocation settlementLoc, boolean isFree) {
		super(gameID, facade);
		this.playerIndex = playerIndex;
		this.settlementLocation = settlementLoc;
		this.isFree = isFree;
	}

	@Override
	/**
	 * Executes the command for building a settlement.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException{
		Object o;
		try {
			o = this.getFacade().buildSettlement(this.getGameID(), playerIndex, isFree, settlementLocation);
			Persistence.getInstance().getGameDAO().addCommand(this.getGameID(), this);
			return o;
		} catch(InvalidActionException e){
			throw e;
		}
	}

}
