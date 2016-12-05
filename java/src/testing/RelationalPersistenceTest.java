package testing;

import client.admin.User;
import client.data.PlayerInfo;
import org.junit.Test;
import static org.junit.Assert.*;

import plugins.relational.CommandDAO;
import plugins.relational.DatabaseHelper;
import plugins.relational.GameDAO;
import plugins.relational.UserDAO;
import server.command.Command;
import server.command.moves.RollNumberCommand;
import server.command.moves.SendChatCommand;
import server.model.ServerGame;
import shared.definitions.CatanColor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 */
public class RelationalPersistenceTest {

	private String TEST_DATABASE = "test.db";


	@Test
	public void testDatabaseConnection() {

		try (Connection conn = DatabaseHelper.getConnection(TEST_DATABASE)) {

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

		DatabaseHelper.reset(TEST_DATABASE);

		try (Connection connection = DatabaseHelper.getConnection(TEST_DATABASE)) {

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

		DatabaseHelper.reset(TEST_DATABASE);

		RollNumberCommand command = new RollNumberCommand(null, 1, 0, 6);

		CommandDAO commandDAO = new CommandDAO();
		commandDAO.setDatabaseName(TEST_DATABASE);

		commandDAO.addCommand(command.getGameID(), command);

		try (Connection connection = DatabaseHelper.getConnection(TEST_DATABASE);
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

		DatabaseHelper.reset(TEST_DATABASE);

		CommandDAO commandDAO = new CommandDAO();
		commandDAO.setDatabaseName(TEST_DATABASE);

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

		DatabaseHelper.reset(TEST_DATABASE);

		User user = new User("Test", "test");
		UserDAO userDAO = new UserDAO();

		userDAO.createUser(user);
		userDAO.setDatabaseName(TEST_DATABASE);

		try (Connection connection = DatabaseHelper.getConnection(TEST_DATABASE);
			 Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery("SELECT user FROM users");
			while (resultSet.next()) {
				User retrieved = (User) DatabaseHelper.getObject(resultSet.getBytes(1));

				assertNotNull(retrieved);
				assertEquals(user.getUsername(), retrieved.getUsername());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testGetUsers() {

		DatabaseHelper.reset(TEST_DATABASE);

		UserDAO userDAO = new UserDAO();
		userDAO.setDatabaseName(TEST_DATABASE);

		User test = new User("Test", "test");
		userDAO.createUser(test);

		User sam = new User("Sam", "sam");
		sam.setCookie("cookie");
		userDAO.createUser(sam);

		List<User> users = userDAO.getUsers();
		assertEquals(2, users.size());

		assertEquals(test.getUsername(), users.get(0).getUsername());
		assertEquals(sam.getCookie(), users.get(1).getCookie());
	}

	@Test
	public void testAddGame() {

		DatabaseHelper.reset(TEST_DATABASE);

		GameDAO gameDAO = new GameDAO();
		gameDAO.setDatabaseName(TEST_DATABASE);

		ServerGame game = new ServerGame(false, false, false, "test", 0);
		game.addPlayer(0, "Test", CatanColor.BLUE);


		try (Connection connection = DatabaseHelper.getConnection(TEST_DATABASE);
			 Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery("SELECT game FROM games");
			while (resultSet.next()) {
				ServerGame retrieved = (ServerGame) DatabaseHelper.getObject(resultSet.getBytes(1));

				assertNotNull(retrieved);

				assertEquals(game.getGameName(), retrieved.getGameName());
				assertEquals(game.getPlayerList().size(), retrieved.getPlayerList().size());
				assertEquals(game.getPlayerList().get(0).getName(), retrieved.getPlayerList().get(0).getName());

			}
		}
		catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testUpdateGame() {

		DatabaseHelper.reset(TEST_DATABASE);

		GameDAO gameDAO = new GameDAO();
		gameDAO.setDatabaseName(TEST_DATABASE);

		ServerGame first = new ServerGame(false, false, false, "First", 0);
		gameDAO.createGame(first);

		first.addPlayer(0, "Added", CatanColor.BLUE);

		gameDAO.saveGame(first);

		try (Connection connection = DatabaseHelper.getConnection(TEST_DATABASE);
			 Statement statement = connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery("SELECT game FROM games");
			while (resultSet.next()) {
				ServerGame retrieved = (ServerGame) DatabaseHelper.getObject(resultSet.getBytes(1));

				assertNotNull(retrieved);

				assertEquals(first.getPlayerList().size(), retrieved.getPlayerList().size());
				assertEquals(first.getPlayerList().get(0).getName(), retrieved.getPlayerList().get(0).getName());

			}
		}
		catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testGetGames() {

		DatabaseHelper.reset(TEST_DATABASE);

		GameDAO gameDAO = new GameDAO();
		gameDAO.setDatabaseName(TEST_DATABASE);

		ServerGame first = new ServerGame(false, false, false, "First", 0);
		gameDAO.createGame(first);

		ServerGame second = new ServerGame(true, true, true, "Second", 1);
		gameDAO.createGame(second);

		List<ServerGame> games = gameDAO.getGames();
		assertEquals(2, games.size());

		assertEquals(first.getGameName(), games.get(0).getGameName());
		assertEquals(second.getGameName(), games.get(1).getGameName());
	}
}
