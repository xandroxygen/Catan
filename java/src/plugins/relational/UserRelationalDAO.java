package plugins.relational;


import client.admin.User;
import server.persistence.IUserDAO;

import java.util.List;

public class UserRelationalDAO implements IUserDAO {
	/**
	 * Stores a user for the persistent provider
	 *
	 * @param user the user to add to storage
	 */
	@Override
	public void createUser(User user) {

	}

	/**
	 * Returns list of users from the persistent provider
	 *
	 * @return list of users
	 */
	@Override
	public List<User> getUsers() {
		return null;
	}
}
