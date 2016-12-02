package plugins.serialized;

import java.util.List;

import server.model.ServerGame;
import server.persistence.IGameDAO;

public class GameDAO implements IGameDAO {

	@Override
	public void createGame(ServerGame game) {
		// TODO Auto-generated method stub
		
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

	@Override
	public List<ServerGame> getGames() {
		// TODO Auto-generated method stub
		return null;
	}
}
