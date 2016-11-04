package server.command.moves;

import client.model.InvalidActionException;
import server.command.Command;
import server.facade.IServerFacade;

public class BuyDevCardCommand extends Command{

	public BuyDevCardCommand(IServerFacade facade) {
		super(facade);
	}

	@Override
	public Object execute() {
		return this.getFacade().buyDevCard();		
	}
}
