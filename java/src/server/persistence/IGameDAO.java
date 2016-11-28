package server.persistence;

import java.util.List;

import server.model.ServerGame;


public interface IGameDAO {
	/**
	 * Stores a game into the persistent provider
	 * @param user the user to add to storage
	 */
	public void createGame(ServerGame game);
	
	/**
	 * Returns list of games from the persistent provider
	 */
	public List<ServerGame> getGames();
}
