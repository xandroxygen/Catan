package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import server.persistence.Persistence;
import shared.definitions.ResourceType;
import shared.model.InvalidActionException;

public class MonopolyCommand extends Command{
	
	/**
	 * the ID of the player playing the Monopoly card
	 */
	private int playerIndex;
	/**
	 * the resource to be taken from the other players
	 */
	private ResourceType resource;
	
	public MonopolyCommand(IServerFacade facade, int gameID, int playerIndex, ResourceType resource) {
		super(gameID, facade);
		this.playerIndex = playerIndex;
		this.resource = resource;
	}

	@Override
	/**
	 * Executes the command for playing a Monopoly card.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException {
		try {
			Object o = this.getFacade().playMonopoly(this.getGameID(), playerIndex, resource);
			Persistence.getInstance().getGameDAO().addCommand(this.getGameID(), this);
			return o;
		}
		catch(InvalidActionException e) {
			throw e;
		}
	}

}
