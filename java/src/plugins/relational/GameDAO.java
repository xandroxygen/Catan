package plugins.relational;


import server.model.ServerGame;
import server.persistence.IGameDAO;

import java.util.List;

public class GameDAO implements IGameDAO{
	/**
	 * Stores a game into the persistent provider
	 *
	 * @param game the game to add to storage
	 */
	@Override
	public void createGame(ServerGame game) {

	}

	/**
	 * Saves an updated game into the persistence provider.
	 * <p>
	 * Called when N commands have been executed. Deletes current
	 * game state and commands for the game. Saves new game state.
	 *
	 * @param game
	 */
	@Override
	public void saveGame(ServerGame game) {

	}

	/**
	 * Returns list of games from the persistent provider
	 */
	@Override
	public List<ServerGame> getGames() {
		return null;
	}
}
