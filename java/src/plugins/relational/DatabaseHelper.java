package plugins.relational;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.Scanner;

/**
 * Creates database and handles transactions.
 * Referenced by all DAOs.
 */
public class DatabaseHelper {


	/*
		methods needed
		- get connection
		- get blob from object
		- get object from blob
		- reset - clears then creates
			- should be called only by user
			- perhaps better as an ant target that deletes file and restarts server?
			- takes same command line param for sqlite vs serialized

		sqlConfig.txt
		- drop all tables if exists
		- create all tables
		- accessed as resource

		catan.sqlite
		- accessed as resource?
		- can this be included in the plugin jar file? definitely for production.

		there should be initial file with 4 demo players, and a demo game or two

		actual statements and executing should be done in the DAOs

		should actual database data be stored under version control?
			no. neither should the file.
			or, initial file should be. but ignore the rest of the changes
			yes because initial database will contain blobs, and those are harder to bootstrap with SQL statements.

		when server starts up
			there will always be an existing database, with initial data in it

	 */

	/**
	 * Opens and returns a new SQL connection
	 * @return
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:java/src/plugins/relational/catan.db"); // TODO this may change with plugins
	}

	/**
	 * Transforms object into array of bytes used to store in
	 * database as a BLOB.
	 * @param object Any object that implements Serializable
	 * @return an array of bytes
	 * @return null on failure
	 */
	public static byte[] getBlob(Object object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			return baos.toByteArray();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Transforms a BLOB as array of bytes into an object.
	 * @return object ready to be cast
	 * @return null on failure
	 */
	public static Object getObject(byte[] blob) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(blob);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Clears database and reloads initial data.
	 *
	 * Somehow, this will wipe the database, and
	 * reload it with the initial data.
	 */
	public static void reset() {


		// TODO: for now, this will just run the config script. later it will load initial data.
		URL url = DatabaseHelper.class.getResource("dbConfig.txt");
		File config = null;
		try {
			config = new File(url.toURI());
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}

		try (Scanner scanner = new Scanner(config);
				Connection connection = DatabaseHelper.getConnection();
			 	Statement statement = connection.createStatement()) {

			// split SQL statements
			scanner.useDelimiter(";");

			// execute SQL
			while (scanner.hasNext()) {
				String sql = scanner.next();
				statement.execute(sql);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
}
