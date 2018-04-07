package com.linkedin.replica.chat.messaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import com.linkedin.replica.chat.config.Configuration;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class BroadcastChannels {
	private Connection connection;
	private Channel sendBroadcastChannel;
	private Channel receiveBroadcastChannel;
	
	private static BroadcastChannels instance;
    private final String EXCHANGE_NAME = Configuration.getInstance().getAppConfigProp("rabbitmq.queue.broadcast");

    private BroadcastChannels() throws IOException, TimeoutException{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername(Configuration.getInstance().getAppConfigProp("rabbitmq.username"));
		factory.setPassword(Configuration.getInstance().getAppConfigProp("rabbitmq.password"));
		factory.setHost(Configuration.getInstance().getAppConfigProp("rabbitmq.ip"));
		
		Connection connection = factory.newConnection();
		
		sendBroadcastChannel = connection.createChannel();
		receiveBroadcastChannel = connection.createChannel();
		
        // declare the queue if it does not exist
		sendBroadcastChannel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		receiveBroadcastChannel.exchangeDeclare(EXCHANGE_NAME, "fanout");
	}
	
	public static BroadcastChannels getInstance() throws IOException, TimeoutException{
		if(instance == null){
			synchronized (BroadcastChannels.class) {
				if(instance == null)
					instance = new BroadcastChannels();
			}
		}
		return instance;
	}
	
	public Channel sendBroadcastChannel() {
		return sendBroadcastChannel;
	}

	public Channel receiveBroadcastChannel() {
		return receiveBroadcastChannel;
	}

	public void closeConnections() throws IOException, TimeoutException {
		sendBroadcastChannel.close();
		receiveBroadcastChannel.close();
		connection.close();
	}
	
}
