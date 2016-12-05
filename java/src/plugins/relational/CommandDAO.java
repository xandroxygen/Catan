package plugins.relational;


import server.command.Command;
import server.persistence.ICommandDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandDAO implements ICommandDAO{

	private Map<Integer, Integer> commandCounts;
	private String databaseName;

	// --- SQL STATEMENTS ---
	private static final String INSERT = "INSERT INTO commands(game_id, command) VALUES(?,?)";
	private static final String SELECT = "SELECT command FROM commands WHERE game_id = ?";
	private static final String DELETE = "DELETE FROM commands WHERE game_id = ?";

	public CommandDAO() {
		commandCounts = new HashMap<>();
		databaseName = DatabaseHelper.DEFAULT_DATABASE;
	}

	/**
	 * Stores command into the persistent provider
	 */
	@Override
	public void addCommand(int gameID, Command command) {

		DatabaseHelper.reset();

		try(Connection connection = DatabaseHelper.getConnection(databaseName);
			PreparedStatement statement = connection.prepareStatement(INSERT)) {

			statement.setInt(1,gameID);
			statement.setBytes(2, DatabaseHelper.getBlob(command));
			statement.executeUpdate();

			// increment command count for this game
			commandCounts.put(gameID, (commandCounts.containsKey(gameID) ? commandCounts.get(gameID) : 0) + 1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a list of commands from the persistent provider
	 *
	 * @param gameID id of the game
	 * @return list of commands
	 */
	@Override
	public List<Command> getCommands(int gameID) {

		List<Command> commands = new ArrayList<>();

		try(Connection connection = DatabaseHelper.getConnection(databaseName);
			PreparedStatement statement = connection.prepareStatement(SELECT)) {

			statement.setInt(1,gameID);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				byte[] blob = result.getBytes("command");
				Command command = (Command) DatabaseHelper.getObject(blob);
				commands.add(command);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return commands;
	}

	/**
	 * Removes all commands from the persistent provider for the specified game
	 *
	 * @param gameID id of the game
	 */
	@Override
	public void clearCommands(int gameID) {

		try(Connection connection = DatabaseHelper.getConnection(databaseName);
			PreparedStatement statement = connection.prepareStatement(DELETE)) {

			statement.setInt(1,gameID);
			statement.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param gameID
	 * @return int count command
	 */
	@Override
	public int getCommandCount(int gameID) {
		return commandCounts.get(gameID);
	}

	/**
	 * Resets command count to 0. Used when game is saved and checkpoint is reached.
	 *
	 * @param gameID
	 */
	@Override
	public void resetCommandCount(int gameID) {
		commandCounts.put(gameID, 0);
	}

	/**
	 * @return the database being used
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * Used for mocking purposes to define a different database.
	 * @param databaseName
	 */
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
}
