package com.linkedin.replica.chat.main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.database.DatabaseConnection;
import com.linkedin.replica.chat.realtime.RealtimeDataHandler;
import com.linkedin.replica.chat.realtime.RealtimeServer;
import com.linkedin.replica.chat.utils.JwtUtilities;

public class Main {

	static  DatabaseConnection dbInstance;

	public static void start() throws IOException, NoSuchAlgorithmException, InterruptedException {

		// create singleton instance of Configuration class
		String rootPath = "src/main/resources/";
		Configuration.init(rootPath+"app.config",
				rootPath+ "arango.config",
				rootPath+ "commands.config",
				rootPath+ "controller.config");

		// create singleton instance of DatabaseConnection
		DatabaseConnection.init();

		JwtUtilities.initKey();

		RealtimeDataHandler.init();

		String chatIp = Configuration.getInstance().getAppConfigProp("chat.ip");
		int chatPort = Integer.parseInt(Configuration.getInstance().getAppConfigProp("chat.port"));
		new RealtimeServer(chatIp, chatPort);
	}

	public static void shutdown() {
		dbInstance.closeConnections();
	}

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {
		start();
	}
}