package plugins.serialized;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import server.model.ServerGame;
import server.model.ServerModel;
import server.persistence.IGameDAO;

public class GameDAO implements IGameDAO {

	private int maxCommandCount;

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
	    	  File[] listOfFiles = folder.listFiles(); 
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
}
