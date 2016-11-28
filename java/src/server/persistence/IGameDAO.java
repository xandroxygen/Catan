package server.persistence;

import server.model.ServerGame;

import java.util.List;


public interface IGameDAO {
	/**
	 * Stores a game into the persistent provider
	 * @param game the game to add to storage
	 */
	public void createGame(ServerGame game);

	
	/**
	 * Returns list of games from the persistent provider
	 */
	public List<ServerGame> getGames();
}
