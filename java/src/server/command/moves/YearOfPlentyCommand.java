package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.definitions.ResourceType;
import shared.model.InvalidActionException;

public class YearOfPlentyCommand extends Command {

	/**
	 * the ID of the player playing the Year of Plenty card
	 */
	private int playerID;
	/**
	 * the first resource the player wants to receive 
	 */
	private ResourceType resource1;
	/**
	 * the second resource the player wants to receive
	 */
	private ResourceType resource2;
	
	public YearOfPlentyCommand(IServerFacade facade, int gameID, int playerID, ResourceType resource1, ResourceType resource2) {
		super(gameID, facade);
		this.playerID = playerID;
		this.resource1 = resource1;
		this.resource2 = resource2;
	}

	@Override
	/**
	 * Executes the command for playing the Year of Plenty card.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException {
		return this.getFacade().playYearOfPlenty(this.getGameID(), playerID, resource1, resource2);	
	}

}
