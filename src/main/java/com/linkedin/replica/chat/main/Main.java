package com.linkedin.replica.chat.main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.database.DatabaseConnection;
import com.linkedin.replica.chat.messaging.BroadcastMessageHandler;
import com.linkedin.replica.chat.messaging.ClientMessagesReceiver;
import com.linkedin.replica.chat.realtime.RealtimeServer;
import com.linkedin.replica.chat.utils.JwtUtilities;

public class Main {

	static DatabaseConnection dbInstance;

	public static void start(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException,
			TimeoutException {

		Configuration.init(args[0], args[1], args[2], args[3]);
		
		// create singleton instance of DatabaseConnection
		DatabaseConnection.init();

		JwtUtilities.initKey();

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
				new BroadcastMessageHandler();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();

		// start RabbitMQ
		new ClientMessagesReceiver();

		// TODO controller
	}

	public static void shutdown() {
		dbInstance.closeConnections();
	}

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException,
			TimeoutException {
		String rootPath = "src/main/resources/";
		String[] s = {rootPath+"app.config", rootPath+ "arango.config", rootPath+ "commands.config", rootPath+ "controller.config"};
		start(s);
	}
}