package com.linkedin.replica.chat.messaging;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.linkedin.replica.chat.config.Configuration;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQChannels {
	private Connection connection;
	private Channel sendBroadcastChannel;
	private Channel receiveBroadcastChannel;
	private Channel sendInterChannel;
	private Channel receiveInterChannel;

	private static RabbitMQChannels instance;
    private final String EXCHANGE_NAME = Configuration.getInstance().getAppConfigProp("rabbitmq.queue.broadcast");

    private RabbitMQChannels() throws IOException, TimeoutException{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername(Configuration.getInstance().getAppConfigProp("rabbitmq.username"));
		factory.setPassword(Configuration.getInstance().getAppConfigProp("rabbitmq.password"));
		factory.setHost(Configuration.getInstance().getAppConfigProp("rabbitmq.ip"));
		
		connection = factory.newConnection();
		
		sendBroadcastChannel = connection.createChannel();
		receiveBroadcastChannel = connection.createChannel();

		sendInterChannel = connection.createChannel();
		receiveInterChannel = connection.createChannel();
		
        // declare the queue if it does not exist
		sendBroadcastChannel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		receiveBroadcastChannel.exchangeDeclare(EXCHANGE_NAME, "fanout");
	}
	
	public static RabbitMQChannels getInstance() throws IOException, TimeoutException{
		if(instance == null){
			synchronized (RabbitMQChannels.class) {
				if(instance == null)
					instance = new RabbitMQChannels();
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

	public Channel sendInterChannel() {
		return sendInterChannel;
	}

	public Channel receiveInterChannel() {
		return receiveInterChannel;
	}

	public void closeConnections() throws IOException, TimeoutException {
		sendBroadcastChannel.close();
		receiveBroadcastChannel.close();
		connection.close();
	}
	
}
