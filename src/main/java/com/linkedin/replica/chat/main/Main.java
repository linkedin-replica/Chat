package com.linkedin.replica.chat.main;

import java.io.IOException;

import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.database.DatabaseConnection;

public class Main {

	static  DatabaseConnection dbInstance;

	public static void start() throws  IOException {

		// create singleton instance of Configuration class
		String rootPath = "src/main/resources/";
		Configuration.init(rootPath+"app.config",
				rootPath+ "arango.config",
				rootPath+ "commands.config",
				rootPath+ "controller.config");
		Configuration.getInstance();

		// create singleton instance of DatabaseConnection
		DatabaseConnection.init();
		dbInstance =  DatabaseConnection.getInstance();
	}

	public static void shutdown() {
		dbInstance.closeConnections();
	}

}