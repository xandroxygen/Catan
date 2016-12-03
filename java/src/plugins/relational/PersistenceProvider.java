package plugins.relational;


import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

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
	
	private final String  FILE_PATH = "java\\src\\plugins\\relational";
	private final String GAME_DAO_PATH = "plugins.relational.GameDAO";
	private final String USER_DAO_PATH = "plugins.relational.UserDAO";
	private final String COMMAND_DAO_PATH = "plugins.relational.CommandDAO";
	
	public PersistenceProvider() {
		dbHelper = new DBHelper();
		
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
		//dbHelper.clear();
	}

	@Override
	public IGameDAO getGameDAO() {
		return this.gameDAO;
	}

	@Override
	public IUserDAO getUserDAO() {
		return this.userDAO;
	}

	@Override
	public ICommandDAO getCommandDAO() {
		return this.commandDAO;
	}
	
	@Override
	public int returnFive() {
		// TODO Auto-generated method stub
		return 5;
	}
}
