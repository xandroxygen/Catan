package plugins.relational;


import server.command.Command;
import server.persistence.ICommandDAO;

import java.util.List;

public class CommandRelationalDAO implements ICommandDAO{

	/**
	 * Stores command into the persistent provider
	 */
	@Override
	public void addCommand() {

	}

	/**
	 * Returns a list of commands from the persistent provider
	 *
	 * @param gameID id of the game
	 * @return list of commands
	 */
	@Override
	public List<Command> getCommands(int gameID) {
		return null;
	}

	/**
	 * Removes all commands from the persistent provider for the specified game
	 *
	 * @param gameID id of the game
	 */
	@Override
	public void clearCommands(int gameID) {

	}

	/**
	 * @return int count command
	 */
	@Override
	public int getCommandCount() {
		return 0;
	}

	/**
	 * Resets command count to 0. Used when game is saved and checkpoint is reached.
	 */
	@Override
	public void resetCommandCount() {

	}
}
