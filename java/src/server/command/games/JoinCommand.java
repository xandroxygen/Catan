package server.command.games;

import server.command.Command;
import server.facade.IServerFacade;
import server.persistence.Persistence;
import shared.definitions.CatanColor;
import shared.model.InvalidActionException;

public class JoinCommand extends Command {
	private int playerIndex;
	private CatanColor color;

	public JoinCommand(IServerFacade facade, int gameID, int playerIndex, CatanColor color ) {
		super(gameID, facade);
		this.playerIndex = playerIndex;
		this.color = color;
	}

	@Override
	public Object execute() throws InvalidActionException {
		try {
			Object o = this.getFacade().gamesJoin(this.getGameID(), playerIndex, color);
			Persistence.getInstance().getGameDAO().addCommand(this.getGameID(), this);
			return o;
		} 
		catch (InvalidActionException e) {
			throw e;
		}
	}

}
