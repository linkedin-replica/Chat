package database;

import com.arangodb.ArangoDB;

import config.ConfigReader;

import java.io.IOException;

public class DatabaseConnection {
	private static ArangoDB arangoDriver;
	private ConfigReader config;

	private volatile static DatabaseConnection dbConnection;

	private DatabaseConnection() throws IOException {
		config = ConfigReader.getInstance();
		initializeArangoDB();
	}

	private void initializeArangoDB() {
		arangoDriver = new ArangoDB.Builder().user(config.getArangoConfig("arangodb.user"))
				.password(config.getArangoConfig("arangodb.password")).build();
	}

	public static DatabaseConnection getDBConnection() throws IOException {
		if (dbConnection == null) {
			synchronized (DatabaseConnection.class) {
				if (dbConnection == null)
					dbConnection = new DatabaseConnection();
			}
		}
		return dbConnection;
	}

	public ArangoDB getArangoDriver() {
		return arangoDriver;
	}

	public static void closeConnections() {
		// TODO Auto-generated method stub
		arangoDriver.shutdown();
	}
}