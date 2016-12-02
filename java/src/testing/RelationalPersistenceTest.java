package testing;

import client.admin.User;
import client.data.PlayerInfo;
import org.junit.Test;
import static org.junit.Assert.*;

import plugins.relational.CommandRelationalDAO;
import plugins.relational.DatabaseHelper;
import server.command.Command;
import server.command.moves.RollNumberCommand;
import server.command.moves.SendChatCommand;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 */
public class RelationalPersistenceTest {


	@Test
	public void testDatabaseConnection() {

		try (Connection conn = DatabaseHelper.getConnection()) {

			System.out.print("Database Connection established");
			assertTrue(true);
		}
		catch (SQLException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testDatabaseCreation() {

		DatabaseHelper.reset();

		try (Connection connection = DatabaseHelper.getConnection()) {

			ResultSet games = connection.getMetaData().getTables(null, null, "games", null);
			assertTrue(games.next());
		}
		catch (SQLException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testObjectBlobCasting() {

		PlayerInfo test = new PlayerInfo(1,1,"test",null);
		byte[] blob = DatabaseHelper.getBlob(test);

		assertNotNull(blob);

		PlayerInfo cast = (PlayerInfo) DatabaseHelper.getObject(blob);

		assertNotNull(cast);
		assertEquals(test.getName(), cast.getName());
	}

	@Test
	public void testAddCommand() {

		RollNumberCommand command = new RollNumberCommand(null, 1, 0, 6);

		CommandRelationalDAO commandDAO = new CommandRelationalDAO();

		commandDAO.addCommand(command.getGameID(), command);

		try (Connection connection = DatabaseHelper.getConnection();
				Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery("SELECT command FROM commands");
			while (resultSet.next()) {
				Command retrieved = (Command) DatabaseHelper.getObject(resultSet.getBytes(1));

				assertNotNull(retrieved);
				assertEquals(command.getGameID(), retrieved.getGameID());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testGetCommands() {

		CommandRelationalDAO commandDAO = new CommandRelationalDAO();

		RollNumberCommand rollNumberCommand = new RollNumberCommand(null, 0, 0, 6);
		commandDAO.addCommand(rollNumberCommand.getGameID(), rollNumberCommand);

		SendChatCommand sendChatCommand = new SendChatCommand(null, 0, 0, "chat");
		commandDAO.addCommand(sendChatCommand.getGameID(), sendChatCommand);

		List<Command> commands = commandDAO.getCommands(0);
		assertEquals(2, commands.size());

		assertEquals(rollNumberCommand.getGameID(), commands.get(0).getGameID());
	}

	@Test
	public void testAddUser() {

		User user = new User("Test", "test");


	}
}
