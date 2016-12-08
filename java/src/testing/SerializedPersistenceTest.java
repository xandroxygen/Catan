package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import client.admin.User;
import plugins.serialized.CommandDAO;
import plugins.serialized.GameDAO;
import plugins.serialized.UserDAO;
import server.command.Command;
import server.command.moves.MonumentCommand;
import server.command.moves.RollNumberCommand;
import server.command.moves.SendChatCommand;
import server.model.ServerGame;
import server.model.ServerModel;

public class SerializedPersistenceTest {
	UserDAO userDAO = new UserDAO();
	GameDAO gameDAO = new GameDAO();

	@Test
	public void testAddGame() {
		
		// Create games
		ServerGame game1 = new ServerGame(false, false, false, "First Game", 0);
		ServerGame game2 = new ServerGame(false, true, false, "Second Game", 1);
		
		// Add them
		gameDAO.createGame(game1);
		gameDAO.createGame(game2);
		
		// Retrieve them
		List<ServerGame> games = gameDAO.getGames();
		assertTrue(games.get(0).getGameId() == 0);
		assertTrue(games.get(0).getGameName().equals("First Game"));
		assertTrue(games.get(1).getGameId() == 1);
		assertTrue(games.get(1).getGameName().equals("Second Game"));
		
		// Clear them
		gameDAO.reset();
		assertTrue(gameDAO.getGames().size() == 0);
	}
	
	@Test
	public void testAddUser() {
		
		// Create users
		
		User u1 = new User("John","pass");
		User u2 = new User("Sam","word");
		
		ArrayList<User> newUsers = new ArrayList<>(Arrays.asList(u1,u2));
		ServerModel.getInstance().setUsers(newUsers);
		
		// Add them
		userDAO.createUser(u1);
		userDAO.createUser(u2);
		
		// Retrieve them
		List<User> users = userDAO.getUsers();
		assertTrue(users.get(0).getUsername().equals("John"));
		assertTrue(users.get(0).getPassword().equals("pass"));
		assertTrue(users.get(1).getUsername().equals("Sam"));
		assertTrue(users.get(1).getPassword().equals("word"));
		
		// Clear them
		userDAO.reset();
		assertTrue(userDAO.getUsers().size() == 0);
	}
	
	@Test
	public void testAddCommand() {
		
		gameDAO.setMaxCommandCount(3);
		CommandDAO commandDAO = new CommandDAO();
		gameDAO.setCommandDAO(commandDAO);
		
		// Create games
		ServerGame game1 = new ServerGame(false, false, false, "First Game", 0);
		ServerGame game2 = new ServerGame(false, true, false, "Second Game", 1);
		
		ArrayList<ServerGame> newGames = new ArrayList<>(Arrays.asList(game1,game2));
		ServerModel.getInstance().setGames(newGames);
		
		// Add them
		gameDAO.createGame(game1);
		gameDAO.createGame(game2);
		
		// Create commands
		RollNumberCommand rollNumberCommand = new RollNumberCommand(null, 0, 0, 6);
		SendChatCommand sendChatCommand = new SendChatCommand(null, 0, 0, "chat");
		MonumentCommand monCommand = new MonumentCommand(null, 1, 3);
		
		// Add them
		gameDAO.addCommand(rollNumberCommand.getGameID(), rollNumberCommand);
		gameDAO.addCommand(sendChatCommand.getGameID(), sendChatCommand);
		gameDAO.addCommand(monCommand.getGameID(), monCommand);
		
		// Retrieve them
		List<Command> commands0 = gameDAO.getCommandDAO().getCommands(0);
		assertTrue(commands0.get(0).getGameID() == 0);
		assertTrue(commands0.get(1).getGameID() == 0);
		assertTrue(commands0.size() == 2);
		List<Command> commands1 = gameDAO.getCommandDAO().getCommands(1);
		assertTrue(commands1.get(0).getGameID() == 1);
		assertTrue(commands1.size() == 1);
		
		// Add another to hit limit
		MonumentCommand monCommand0 = new MonumentCommand(null, 0, 3);
		gameDAO.addCommand(monCommand0.getGameID(), monCommand0);
		commands0 = gameDAO.getCommandDAO().getCommands(0);
		assertTrue(commands0.size() == 0);
		
		// Add them again
		gameDAO.addCommand(rollNumberCommand.getGameID(), rollNumberCommand);
		commands0 = gameDAO.getCommandDAO().getCommands(0);
		assertTrue(commands0.get(0).getGameID() == 0);
		assertTrue(commands0.size() == 1);
		
		// Clear them
		gameDAO.reset();
		commands0 = gameDAO.getCommandDAO().getCommands(0);
		assertTrue(commands0.size() == 0);
		commands1 = gameDAO.getCommandDAO().getCommands(1);
		assertTrue(commands1.size() == 0);
	}

}
