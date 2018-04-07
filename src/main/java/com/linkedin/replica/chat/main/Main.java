package com.linkedin.replica.chat.main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.database.DatabaseConnection;
import com.linkedin.replica.chat.messaging.BroadcastMessageHandler;
import com.linkedin.replica.chat.realtime.RealtimeDataHandler;
import com.linkedin.replica.chat.realtime.RealtimeServer;
import com.linkedin.replica.chat.utils.JwtUtilities;

public class Main {

	static  DatabaseConnection dbInstance;

	public static void start() throws IOException, NoSuchAlgorithmException, InterruptedException, TimeoutException {

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
		
		new Thread(() -> {
			String chatIp = Configuration.getInstance().getAppConfigProp("chat.ip");
			int chatPort = Integer.parseInt(Configuration.getInstance().getAppConfigProp("chat.port"));
			try {
				new RealtimeServer(chatIp, chatPort);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();

		new Thread(() -> {
			try {
				BroadcastMessageHandler handler = new BroadcastMessageHandler();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
		
	}

	public static void shutdown() {
		dbInstance.closeConnections();
	}

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {
		start();
	}
}