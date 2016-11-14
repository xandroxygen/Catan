package server.command.moves;

import java.util.HashMap;
import java.util.Map;

import server.command.Command;
import server.facade.IServerFacade;
import shared.definitions.ResourceType;
import shared.model.InvalidActionException;

public class OfferTradeCommand extends Command{
	
	/**
	 * the ID of the person offering the trade
	 */
	private int senderID;
	/**
	 * <pre>
	 * the cards to send and the cards to receive:
	 *  negative means the cards will be received by the sender,
	 *  positive means the cards will be sent by the sender
	 * </pre>
	 */
	private HashMap<ResourceType, Integer> offer;
	/**
	 * the ID of the player receiving the offer
	 */
	private int receiverID;
	
	public OfferTradeCommand(IServerFacade facade, int gameID, int senderID, int receiverID, HashMap<ResourceType, Integer> offer) {
		super(gameID, facade);
		this.senderID = senderID;
		this.receiverID = receiverID;
		this.offer = offer;
	}

	@Override
	/**
	 * Executes the command for offering a trade to a player.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException {
		return this.getFacade().offerTrade(this.getGameID(), senderID, receiverID, offer);
	}

}
