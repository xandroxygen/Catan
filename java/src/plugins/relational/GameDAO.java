package plugins.relational;


import server.command.Command;
import server.model.ServerGame;
import server.persistence.ICommandDAO;
import server.persistence.IGameDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GameDAO implements IGameDAO {

	private int maxCommandCount;
	private ICommandDAO commandDAO;

	// --- SQL STATEMENTS ---
	private static final String INSERT = "INSERT INTO games(game) VALUES(?)";
	private static final String SELECT = "SELECT game FROM games";
	private static final String UPDATE = "UPDATES games SET game = ? WHERE id = ?";

	/**
	 * Stores a game into the persistent provider
	 *
	 * @param game the game to add to storage
	 */
	@Override
	public void createGame(ServerGame game) {

		try(Connection connection = DatabaseHelper.getConnection();
			PreparedStatement statement = connection.prepareStatement(INSERT)) {

			statement.setBytes(1, DatabaseHelper.getBlob(game));
			statement.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves an updated game into the persistence provider.
	 * <p>
	 * Called when N commands have been executed. Updates game blob in database.
	 *
	 * @param game
	 */
	@Override
	public void saveGame(ServerGame game) {

		try(Connection connection = DatabaseHelper.getConnection();
			PreparedStatement statement = connection.prepareStatement(UPDATE)) {

			statement.setBytes(1, DatabaseHelper.getBlob(game));
			statement.setInt(2, game.getGameId());
			statement.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns list of games from the persistent provider
	 */
	@Override
	public List<ServerGame> getGames() {
		List<ServerGame> games = new ArrayList<>();

		try(Connection connection = DatabaseHelper.getConnection();
			Statement statement = connection.createStatement()) {

			ResultSet result = statement.executeQuery(SELECT);

			while (result.next()) {
				byte[] blob = result.getBytes("game");
				ServerGame game = (ServerGame) DatabaseHelper.getObject(blob);
				games.add(game);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return games;
	}

	/**
	 * Adds a command to the currently saved game.
	 * <p>
	 * If game has N current commands, saves new game state
	 * and clears list of commands.
	 *
	 * @param gameID
	 * @param command
	 */
	@Override
	public void addCommand(int gameID, Command command) {

	}

	/**
	 * @return the max command count N that was set on the command line.
	 */
	@Override
	public int getMaxCommandCount() {
		return maxCommandCount;
	}

	/**
	 * Sets the max command count that was set on the command line.
	 *
	 * @param count
	 */
	@Override
	public void setMaxCommandCount(int count) {
		maxCommandCount = count;
	}

	/**
	 * @return the game's command DAO
	 */
	@Override
	public ICommandDAO getCommandDAO() {
		return commandDAO;
	}

	/**
	 * Sets the commandDAO.
	 *
	 * @param commandDAO
	 */
	@Override
	public void setCommandDAO(ICommandDAO commandDAO) {
		this.commandDAO = commandDAO;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}
