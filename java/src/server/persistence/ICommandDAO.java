package server.persistence;

import java.util.List;

import server.command.Command;

public interface ICommandDAO {
	
	
	/**
	 * Stores command into the persistent provider
	 */
	public void addCommand();
	
	/**
	 * Returns a list of commands from the persistent provider
	 * @param gameID id of the game
	 * @return list of commands
	 */
	public List<Command> getCommands(int gameID);
	
	/**
	 * Removes all commands from the persistent provider for the specified game
	 * @param gameID id of the game
	 */
	public void clearCommands(int gameID);
}
