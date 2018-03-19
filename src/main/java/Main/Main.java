package Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import config.ConfigReader;
import database.DatabaseConnection;

public class Main {

	public static void start() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {

		// create singleton instance of Configuration class that will hold configuration
		// files paths
		ConfigReader.getInstance();

		// create singleton instance of DatabaseConnection class that is responsible for
		// initiating connections
		// with databases
		DatabaseConnection.getDBConnection().getArangoDriver();
	}

	public static void shutdown() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		DatabaseConnection.closeConnections();
	}

	public static void main(String[] args)
			throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
		Main.start();
	}
}