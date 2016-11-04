package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import server.facade.moves.IMovesServerFacade;

public class SendChatCommand extends Command{
	
	private String message;

	public SendChatCommand(IServerFacade facade, String message) {
		super(facade);
		this.message = message;
	}

	@Override
	public Object execute() {
		return this.getFacade().sendChat(message);
		
	}

}
