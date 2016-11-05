package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.model.InvalidActionException;

public class SendChatCommand extends Command{
	
	private int playerID;
	private String message;

	public SendChatCommand(IServerFacade facade, int gameID, int playerID, String message) {
		super(gameID, facade);
		this.playerID = playerID;
		this.message = message;
	}

	@Override
	public Object execute() throws InvalidActionException {
		return this.getFacade().sendChat(this.getGameID(), playerID, message);
	}

}
