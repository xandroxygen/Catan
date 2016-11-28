package server.persistence;

import java.util.List;

import client.admin.User;

public interface IUserDAO {
	
	/**
	 * Stores a user for the persistent provider
	 * @param user the user to add to storage
	 */
	public void createUser(User user);
	
	/**
	 * Returns list of users from the persistent provider
	 * @return list of users
	 */
	public List<User> getUsers();
	
}
