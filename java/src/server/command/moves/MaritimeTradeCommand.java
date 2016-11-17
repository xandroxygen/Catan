package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.definitions.ResourceType;
import shared.model.InvalidActionException;

public class MaritimeTradeCommand extends Command {
	
	/**
	 * the ID of the player performing Maritime trade
	 */
	private int playerIndex;
	/**
	 * the ratio at which the resources will be traded (could be 2:1, 3:1, or 4:1)
	 */
	private int ratio;
	/**
	 * the resource being sent by the player
	 */
	private ResourceType inputResource;
	/**
	 * the resource to be received by the player
	 */
	private ResourceType outputResource;

	public MaritimeTradeCommand(IServerFacade facade, int gameID, int playerIndex, int ratio, ResourceType inputResource, ResourceType outputResource) {
		super(gameID, facade);
		this.playerIndex = playerIndex;
		this.ratio = ratio;
		this.inputResource = inputResource;
		this.outputResource = outputResource;
	}

	@Override
	/**
	 * Executes the command for maritime trade.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException{
		return this.getFacade().maritimeTrade(this.getGameID(), playerIndex, ratio, inputResource, outputResource);
	}

}
