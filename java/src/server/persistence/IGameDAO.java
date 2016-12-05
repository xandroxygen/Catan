package server.persistence;

import java.util.List;

import server.command.Command;
import server.model.ServerGame;


public interface IGameDAO {
	/**
	 * Stores a game into the persistent provider
	 * @param game the game to add to storage
	 */
	void createGame(ServerGame game);

	/**
	 * Saves an updated game into the persistence provider.
	 *
	 * Called when N commands have been executed. Deletes current
	 * game state and commands for the game. Saves new game state.
	 */
	void saveGame(ServerGame game);

	/**
	 * Returns list of games from the persistent provider
	 */
	List<ServerGame> getGames();

	/**
	 * Adds a command to the currently saved game.
	 *
	 * If game has N current commands, saves new game state
	 * and clears list of commands.
	 * @param gameID
	 * @param command
	 */
	void addCommand(int gameID, Command command);

	/**
	 * @return the max command count N that was set on the command line.
	 */
	int getMaxCommandCount();

	/**
	 * Sets the max command count that was set on the command line.
	 */
	void setMaxCommandCount(int count);

	/**
	 * @return the game's command DAO
	 */
	ICommandDAO getCommandDAO();

	/**
	 * Sets the commandDAO.
	 * @param commandDAO
	 */
	void setCommandDAO(ICommandDAO commandDAO);
}
