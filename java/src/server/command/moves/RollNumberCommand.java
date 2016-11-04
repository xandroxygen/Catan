package server.command.moves;

import server.command.Command;
import server.facade.IServerFacade;

public class RollNumberCommand extends Command {
	
	private int number;

	public RollNumberCommand(IServerFacade facade, int number) {
		super(facade);
		this.number = number;
	}

	@Override
	public Object execute() {
		return this.getFacade().rollNumber(number);
		
	}

}
