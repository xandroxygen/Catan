package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import shared.model.InvalidActionException;

public class SendChatCommand extends Command{
	/**
	 * the ID of the player sending the chat
	 */
	private int playerIndex;
	/**
	 * the message being sent
	 */
	private String message;

	public SendChatCommand(IServerFacade facade, int gameID, int playerIndex, String message) {
		super(gameID, facade);
		this.playerIndex = playerIndex;
		this.message = message;
	}

	@Override
	/**
	 * Executes the command for sending a chat.
	 * 
	 * @post <pre>
	 * 	The command was executed and the result of the command is returned.
	 * </pre>
	 */
	public Object execute() throws InvalidActionException {
		return this.getFacade().sendChat(this.getGameID(), playerIndex, message);
	}

}
