package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;
import server.facade.moves.IMovesServerFacade;

public class SendChatCommand extends Command{
	
	private String message;

	public SendChatCommand(IServerFacade facade) {
		super(facade);
		// TODO Auto-generated constructor stub
	}
	
	public SendChatCommand(IServerFacade facade, String message) {
		super(facade);
		this.message = message;
	}

	@Override
	public void execute() {
		IMovesServerFacade facade = (IMovesServerFacade)getFacade();
		facade.sendChat(message);
		
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
