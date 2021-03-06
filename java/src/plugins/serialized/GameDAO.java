package plugins.serialized;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.command.Command;
import server.model.ServerGame;
import server.model.ServerModel;
import server.persistence.ICommandDAO;
import server.persistence.IGameDAO;

public class GameDAO implements IGameDAO {

	private int maxCommandCount;
	private ICommandDAO commandDAO;
	private Map<Integer, Integer> commandCounts = new HashMap<>();

	@Override
	public void createGame(ServerGame game) {
		try {
			// Serialize Game
				File file = new File("plugins/serialized/save/games/" + game.getGameId() + "/game.ser");
				file.getParentFile().mkdirs();
				file.createNewFile();
				FileOutputStream fileOut = new FileOutputStream(file);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(game);
				out.close();
				fileOut.close();
			}
			catch(IOException i) {
				i.printStackTrace();
			}
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
		try {
			// Serialize Game
				File file = new File("plugins/serialized/save/games/" + game.getGameId() + "/game.ser");
				file.getParentFile().mkdirs();
				file.createNewFile();
				FileOutputStream fileOut = new FileOutputStream(file,false);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(game);
				out.close();
				fileOut.close();
			}
			catch(IOException i) {
				i.printStackTrace();
			}
	}

	@Override
	public List<ServerGame> getGames() {
		List<ServerGame> games = new ArrayList<>();
		ServerGame game = null;
	      try {
	    	  File folder = new File("plugins/serialized/save/games/");
	    	  File[] listOfFiles = folder.listFiles(new FilenameFilter() {
	    	        @Override
	    	        public boolean accept(File dir, String name) {
	    	            return !name.equals(".DS_Store");
	    	        }
	    	  }); 
	    	  for (int i = 0; i < listOfFiles.length; i++) {
	    		  File file = listOfFiles[i];
	    		  FileInputStream fileIn = new FileInputStream(file + "/game.ser");
	    		  ObjectInputStream in = new ObjectInputStream(fileIn);
	    		  game = (ServerGame) in.readObject();
	    		  games.add(game);
	    		  in.close();
	    		  fileIn.close();
	    	 	}
	      }catch(IOException i) {
	         i.printStackTrace();
	      }catch(ClassNotFoundException c) {
	         System.out.println("Model class not found");
	         c.printStackTrace();
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
		// If at max, clear them and save a new game
		if (commandCounts.containsKey(gameID) && commandCounts.get(gameID) == (maxCommandCount-1)) {
			this.commandDAO.clearCommands(gameID);
			this.saveGame(ServerModel.getInstance().getGames(gameID));
			commandCounts.remove(gameID);
		}
		// else add it to the list and increment the count
		else if (commandCounts.containsKey(gameID)) {
			commandCounts.put(gameID, commandCounts.get(gameID)+1);
			this.commandDAO.addCommand(gameID, command);
		}
		else {
			commandCounts.put(gameID, 1);
			this.commandDAO.addCommand(gameID, command);
		}
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
		File folder = new File("plugins/serialized/save/games/");
  	  	File[] listOfFiles = folder.listFiles(new FilenameFilter() {
  	        @Override
  	        public boolean accept(File dir, String name) {
  	            return !name.equals(".DS_Store");
  	        }
  	  	}); 
  	  	for (File f: listOfFiles) {
  	  		deleteDir(f);
  	  	}
	  	
	}
	
	private void deleteDir(File file) {
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            deleteDir(f);
	        }
	    }
	    file.delete();
	}
}
