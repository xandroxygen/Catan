package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import server.persistence.Persistence;
import shared.definitions.ResourceType;
import shared.model.InvalidActionException;

public class YearOfPlentyCommand extends Command {

	/**
	 * the ID of the player playing the Year of Plenty card
	 */
	private int playerIndex;
	/**
	 * the first resource the player wants to receive 
	 */
	private ResourceType resource1;
	/**
	 * the second resource the player wants to receive
	 */
	private ResourceType resource2;
	
	public YearOfPlentyCommand(IServerFacade facade, int gameID, int playerIndex, ResourceType resource1, ResourceType resource2) {
		super(gameID, facade);
		this.playerIndex = playerIndex;
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
		try {
			Object o = this.getFacade().playYearOfPlenty(this.getGameID(), playerIndex, resource1, resource2);	
			Persistence.getInstance().getGameDAO().addCommand(this.getGameID(), this);
			return o;
		}
		catch(InvalidActionException e) {
			throw e;
		}
	}

}
