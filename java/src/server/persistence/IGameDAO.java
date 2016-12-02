package server.persistence;

import java.util.List;

import server.model.ServerGame;


public interface IGameDAO {
	/**
	 * Stores a game into the persistent provider
	 * @param game the game to add to storage
	 */
	public void createGame(ServerGame game);

	/**
	 * Saves an updated game into the persistence provider.
	 *
	 * Called when N commands have been executed. Deletes current
	 * game state and commands for the game. Saves new game state.
	 */
	public void saveGame(ServerGame game);

	/**
	 * Returns list of games from the persistent provider
	 */
	public List<ServerGame> getGames();
}
