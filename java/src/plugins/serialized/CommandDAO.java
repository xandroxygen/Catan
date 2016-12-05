package plugins.serialized;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import client.admin.User;
import server.command.Command;
import server.persistence.ICommandDAO;

public class CommandDAO implements ICommandDAO {

	@Override
	public void addCommand(Command command, int gameID) {
		List<Command> commands = new ArrayList<>();
		try {
			
			// Get game list
			File file = new File("plugins/serialized/save/games/" + gameID + "/command.ser");
			
			// Serialize Game
			file.getParentFile().mkdirs();
			if (!file.createNewFile()) {
				// If file already exists, get current list of commands
				FileInputStream fileIn = new FileInputStream(file);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				commands = (ArrayList<Command>) in.readObject();
				in.close();
				fileIn.close();
			}
			
			// Add command to list and rewrite the list
			commands.add(command);
			FileOutputStream fileOut = new FileOutputStream(file,false);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(commands);
			out.close();
			fileOut.close();
		}catch(IOException i) {
				i.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Command> getCommands(int gameID) {
		List<Command> commands = new ArrayList<>();
		try {
	    	  File file = new File("plugins/serialized/save/games/" + gameID + "/command.ser");
    		  FileInputStream fileIn = new FileInputStream(file);
    		  ObjectInputStream in = new ObjectInputStream(fileIn);
    		  commands = (ArrayList<Command>) in.readObject();
    		  in.close();
    		  fileIn.close();
	      }catch(IOException i) {
	         i.printStackTrace();
	      }catch(ClassNotFoundException c) {
	         c.printStackTrace();
	      }
		return commands;
	}

	@Override
	public void clearCommands(int gameID) {
		try {
			File file = new File("plugins/serialized/save/games/" + gameID + "/command.ser");
  		  	file.delete();
		}catch(Exception e) {
		     e.printStackTrace();
		  }	
	}

	@Override
	public int getCommandCount(int gameID) {
		List<Command> commands = new ArrayList<>();
		try {
	    	  File file = new File("plugins/serialized/save/games/" + gameID + "/command.ser");
    		  FileInputStream fileIn = new FileInputStream(file);
    		  ObjectInputStream in = new ObjectInputStream(fileIn);
    		  commands = (ArrayList<Command>) in.readObject();
    		  in.close();
    		  fileIn.close();
	      }catch(IOException i) {
	         i.printStackTrace();
	      }catch(ClassNotFoundException c) {
	         c.printStackTrace();
	      }
		return commands.size();
	}

	@Override
	public void resetCommandCount(int gameID) {
		try {
		  File file = new File("plugins/serialized/save/games/" + gameID + "/command.ser");
  		  file.delete();
	      }catch(Exception e) {
	         e.printStackTrace();
	      }
	}
}
