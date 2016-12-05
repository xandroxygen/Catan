package plugins.relational;


import server.command.Command;
import server.persistence.ICommandDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CommandDAO implements ICommandDAO{
public class CommandRelationalDAO implements ICommandDAO {

	private int commandCount;

	// --- SQL STATEMENTS ---
	private static final String INSERT = "INSERT INTO commands(game_id, command) VALUES(?,?)";
	private static final String SELECT = "SELECT command FROM commands WHERE game_id = ?";
	private static final String DELETE = "DELETE FROM commands WHERE game_id = ?";

	public CommandRelationalDAO() {
		commandCount = 0;
	}

	/**
	 * Stores command into the persistent provider
	 */
	@Override
	public void addCommand(int gameID, Command command) {

		try(Connection connection = DatabaseHelper.getConnection();
			PreparedStatement statement = connection.prepareStatement(INSERT)) {

			statement.setInt(1,gameID);
			statement.setBytes(2, DatabaseHelper.getBlob(command));
			statement.executeUpdate();
			commandCount++;
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

		try(Connection connection = DatabaseHelper.getConnection();
			PreparedStatement statement = connection.prepareStatement(SELECT)) {

			statement.setInt(1,gameID);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				byte[] blob = result.getBytes("command");
				Command command = (Command) DatabaseHelper.getObject(blob); // TODO will this handle subtypes?
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

		try(Connection connection = DatabaseHelper.getConnection();
			PreparedStatement statement = connection.prepareStatement(DELETE)) {

			statement.setInt(1,gameID);
			statement.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return int count command
	 */
	@Override
	public int getCommandCount() {
		return commandCount;
	}

	/**
	 * Resets command count to 0. Used when game is saved and checkpoint is reached.
	 */
	@Override
	public void resetCommandCount() {
		commandCount = 0;
	}
}
