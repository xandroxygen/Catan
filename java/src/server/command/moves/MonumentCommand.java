package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;

public class MonumentCommand extends Command{

	public MonumentCommand(IServerFacade facade) {
		super(facade);
	}

	@Override
	public Object execute() {
		return this.getFacade().playVictoryPoint();
		
	}

}
