package plugins.serialized;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import client.admin.User;
import server.model.ServerModel;
import server.persistence.IUserDAO;

public class UserDAO implements IUserDAO {

	@Override
	public void createUser(User user) {
		try {	
			// Serialize players
			ServerModel model = ServerModel.getInstance();
			for (int i = 0; i < model.getUsers().size(); i++) {
				User u = model.getUsers().get(i);
				if (u.getUsername().equals(user.getUsername())) {
					File file = new File("plugins/serialized/save/users/" + i + ".ser");
					if (!file.exists()) {
						file.getParentFile().mkdirs();
						file.createNewFile();
						FileOutputStream fileOut = new FileOutputStream(file);
						ObjectOutputStream out = new ObjectOutputStream(fileOut);
						out.writeObject(user);
						out.close();
						fileOut.close();
					}
				}
			}
			
			
		}
		catch(IOException i) {
			i.printStackTrace();
		}
		
	}

	@Override
	public List<User> getUsers() {
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
	      }catch(ClassNotFoundException c) {
	         c.printStackTrace();
	      }
	      return users;
	}
}
