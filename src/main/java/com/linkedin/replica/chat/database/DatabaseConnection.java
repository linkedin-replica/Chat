package com.linkedin.replica.chat.database;

import com.arangodb.ArangoDB;

import com.linkedin.replica.chat.config.Configuration;

import java.io.IOException;

public class DatabaseConnection {
	private ArangoDB arangoDriver;
	private Configuration config;
	private static DatabaseConnection instance;



	private DatabaseConnection() throws IOException{
		config = Configuration.getInstance();
		initializeArangoDB();
	}


	/**
	 * @return A singleton database instance
	 */
	public static DatabaseConnection getInstance() {
		return instance;
	}

	public static void init() throws IOException {
		instance = new DatabaseConnection();
	}

	private void initializeArangoDB() {
		arangoDriver = new ArangoDB.Builder()
				.user(config.getArangoConfigProp("arangodb.user"))
				.password(config.getArangoConfigProp("arangodb.password"))
				.build();
	}

	public void closeConnections() {
		arangoDriver.shutdown();
	}

	public ArangoDB getArangoDriver() {
		return arangoDriver;
	}

}