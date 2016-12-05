package plugins.serialized;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import client.admin.User;
import client.model.Model;
import server.command.Command;
import server.model.ServerGame;
import server.model.ServerModel;

public class FileSerializer {
	
	public static void serializeModel(ServerModel model) {
		/*
		try {
			FileOutputStream fileOut = new FileOutputStream("/save/" + gameID + "/game.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(ServerModel.getInstance());
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in /save/game.ser");
		}
		catch(IOException i) {
			i.printStackTrace();
		}*/
	}
	
	public static void serializeGame(ServerGame game) {
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
	
	public static void readGames() {
		ArrayList<ServerGame> games = new ArrayList<>();
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
	         return;
	      }catch(ClassNotFoundException c) {
	         System.out.println("Model class not found");
	         c.printStackTrace();
	         return;
	      }
	      ServerModel.getInstance().setGames(games);
	}
	
	public static void serializePlayers(ServerModel model) {
		try {
			
			// Serialize players
			for (int i = 0; i < model.getUsers().size(); i++) {
				User user = model.getUsers().get(i);
				File file = new File("plugins/serialized/save/users/" + i + ".ser");
				if (!file.exists()) {
					file.getParentFile().mkdirs();
					file.createNewFile();
					FileOutputStream fileOut = new FileOutputStream(file);
					ObjectOutputStream out = new ObjectOutputStream(fileOut);
					out.writeObject(user);
					out.close();
					fileOut.close();
					System.out.printf("Serialized data is saved in /save/game.ser");
				}
			}
			
			
		}
		catch(IOException i) {
			i.printStackTrace();
		}
	}
	
	public static void readPlayers() {
		ArrayList<User> users = new ArrayList<>();
		User user = null;
	      try {
	    	  File folder = new File("plugins/serialized/save/users/");
	    	  File[] listOfFiles = folder.listFiles(); 
	    	  for (int i = 0; i < listOfFiles.length; i++) {
	    		  File file = listOfFiles[i];
	    		  FileInputStream fileIn = new FileInputStream(file);
	    		  ObjectInputStream in = new ObjectInputStream(fileIn);
	    		  user = (User) in.readObject();
	    		  users.add(user);
	    		  in.close();
	    		  fileIn.close();
	    	 	}
	      }catch(IOException i) {
	         i.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c) {
	         System.out.println("Model class not found");
	         c.printStackTrace();
	         return;
	      }
	      ServerModel.getInstance().setUsers(users);
	}
	
	public static void saveCommand(Command command) {
		try {
			// Serialize Game
				File file = new File("plugins/serialized/save/games/" + command.getGameID() + "/commands.ser");
				file.getParentFile().mkdirs();
				file.createNewFile();
				FileOutputStream fileOut = new FileOutputStream(file,true);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(command);
				out.close();
				fileOut.close();
			}
			catch(IOException i) {
				i.printStackTrace();
			}
	}
}

/**
 * File folder = new File("/save/");
	File[] listOfFiles = folder.listFiles(); 
	List<String> filteredList = new ArrayList<String>(); 
	for (File file : listOfFiles) { 
		if (file.isFile()) { 
			System.out.println(file.getName());
			if(file.getName().toLowerCase().contains(“command”)) {
				filteredList.add(file.getName()) 
			} 
		} 
	}
	*/
