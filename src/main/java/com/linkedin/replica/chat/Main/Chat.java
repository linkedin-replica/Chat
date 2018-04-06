package com.linkedin.replica.chat.Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import com.linkedin.replica.chat.config.ConfigReader;
import com.linkedin.replica.chat.database.DatabaseConnection;

public class Chat {

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
		Chat.start();
	}
}