package server.persistence;

import java.util.List;

import server.command.Command;

public interface ICommandDAO {

	/**
	 * Stores command into the persistent provider
	 */
	void addCommand(int gameId, Command command);

	/**
	 * Returns a list of commands from the persistent provider
	 * @param gameID id of the game
	 * @return list of commands
	 */
	List<Command> getCommands(int gameID);
	
	/**
	 * Removes all commands from the persistent provider for the specified game
	 * @param gameID id of the game
	 */
	void clearCommands(int gameID);

	/**
	 * @return int count command
	 */
	int getCommandCount(int gameID);

	/**
	 * Resets command count to 0. Used when game is saved and checkpoint is reached.
	 */
	void resetCommandCount(int gameID);
}
