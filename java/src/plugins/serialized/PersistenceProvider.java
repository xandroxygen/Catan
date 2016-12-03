package plugins.serialized;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import plugins.relational.DBHelper;
import server.persistence.ClassLoader;
import server.persistence.ICommandDAO;
import server.persistence.IGameDAO;
import server.persistence.IPersistenceProvider;
import server.persistence.IUserDAO;

public class PersistenceProvider implements IPersistenceProvider {
	private IGameDAO gameDAO;
	private IUserDAO userDAO;
	private ICommandDAO commandDAO;
	
	private DBHelper dbHelper;
	
	private final String  FILE_PATH = "java\\src\\plugins\\serialized";
	private final String GAME_DAO_PATH = "plugins.serialized.GameDAO";
	private final String USER_DAO_PATH = "plugins.serialized.UserDAO";
	private final String COMMAND_DAO_PATH = "plugins.serialized.CommandDAO";
	
	public PersistenceProvider() {
		this.dbHelper = new DBHelper();
		
		buildDAOs();
	}
	
	private void buildDAOs() {
		try {
			Class<?> c = ClassLoader.loadClass(FILE_PATH, GAME_DAO_PATH);
			this.gameDAO = (IGameDAO) c.newInstance();
			
			c = ClassLoader.loadClass(FILE_PATH, USER_DAO_PATH);
			this.userDAO = (IUserDAO) c.newInstance();
			
			c = ClassLoader.loadClass(FILE_PATH, COMMAND_DAO_PATH);
			this.commandDAO = (ICommandDAO) c.newInstance();
			commandDAO.getCommandCount();
			
			
//			File file = new File("java\\src\\plugins\\relational");
//			URL[] jarUrl = new URL[]{new URL("file:"+file.getAbsolutePath())};
//			URLClassLoader urlClassLoader = new URLClassLoader(jarUrl);
//			
//			Class<?> g = Class.forName("plugins.serialized.GameDAO", true, urlClassLoader);
//			this.gameDAO = (IGameDAO) g.newInstance();
//			
//			Class<?> u = Class.forName("plugins.serialized.UserDAO", true, urlClassLoader);
//			this.userDAO = (IUserDAO) u.newInstance();
//			
//			Class<?> c = Class.forName("plugins.serialized.CommandDAO", true, urlClassLoader);
//			this.commandDAO = (ICommandDAO) c.newInstance();
//			
//			this.commandDAO.getCommandCount();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void startTransaction() {
//		try {
//			dbHelper.startTransaction();
//		}
//		catch {
//			dbHelper.endTransaction(false);
//		}
	}

	@Override
	public void endTransaction() {
//		try {
//			dbHelper.endTransaction(true);
//		}
//		catch {
//			dbHelper.endTransaction(false);
//		}		
	}

	@Override
	public void clearData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IGameDAO getGameDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUserDAO getUserDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICommandDAO getCommandDAO() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGameDAO(IGameDAO gameDAO) {
		this.gameDAO = (GameDAO) gameDAO;
	}

	@Override
	public void setCommandDAO(ICommandDAO commandDAO) {
		this.commandDAO = (CommandDAO) commandDAO;
	}

	@Override
	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = (UserDAO) userDAO;
	}

	@Override
	public int returnFive() {
		return 5;
	}
}
