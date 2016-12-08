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

	public static final String DEFAULT_DATABASE = "catan.db";
	/**
	 * Opens and returns a new SQL connection with the default database
	 * @return
	 */
	public static Connection getConnection() throws SQLException {
		return getConnection(DEFAULT_DATABASE);
	}

	/**
	 * Opens and returns a new SQL connection with the database specified
	 * @return
	 */
	public static Connection getConnection(String database) throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:resources/database/" + database);
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
	 * Clears database provided and reloads initial data.
	 *
	 * Somehow, this will wipe the database, and
	 * reload it with the initial data.
	 * @param database the name of the database to reset
	 */
	public static void reset(String database) {


		// TODO: for now, this will just run the config script. later it will load initial data.
		File config = new File("resources/dbConfig.txt");

		try (Scanner scanner = new Scanner(config);
				Connection connection = DatabaseHelper.getConnection(database);
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

	/**
	 * Clears default database and reloads initial data.
	 *
	 * Somehow, this will wipe the database, and
	 * reload it with the initial data.
	 */
	public static void reset() {
		reset(DEFAULT_DATABASE);
	}
}
