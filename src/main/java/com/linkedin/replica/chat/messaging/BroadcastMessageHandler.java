package com.linkedin.replica.chat.messaging;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.realtime.RealtimeDataHandler;
import com.linkedin.replica.chat.services.Workers;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class BroadcastMessageHandler {
	private final Configuration configuration = Configuration.getInstance();
	private final RealtimeDataHandler realtimeDataHandler = RealtimeDataHandler.getInstance();
    private final String EXCHANGE_NAME = configuration.getAppConfigProp("rabbitmq.queue.broadcast");
    private final String RABBIT_MQ_IP = configuration.getAppConfigProp("rabbitmq.ip");
    private final String RABBIT_MQ_USERNAME = configuration.getAppConfigProp("rabbitmq.username");
    private final String RABBIT_MQ_PASSWORD = configuration.getAppConfigProp("rabbitmq.password");

    private ConnectionFactory factory;
    private Channel receiveChannel;
    private Channel sendChannel;
    private Connection connection;
    
    public BroadcastMessageHandler() throws IOException, TimeoutException{
		factory = new ConnectionFactory();
        factory.setUsername(RABBIT_MQ_USERNAME);
        factory.setPassword(RABBIT_MQ_PASSWORD);
        factory.setHost(RABBIT_MQ_IP);
        connection = factory.newConnection();
        
        // create channels
        sendChannel = connection.createChannel();
        receiveChannel = connection.createChannel();

        // declare the queue if it does not exist
        sendChannel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        receiveChannel.exchangeDeclare(EXCHANGE_NAME, "fanout");
    }
	
	public void initConsumer() throws IOException{
		String bindingQueue = receiveChannel.queueDeclare().getQueue();
		receiveChannel.queueBind(bindingQueue, EXCHANGE_NAME, "");
		
        // Create the consumer (listener) for the new messages
        Consumer consumer = new DefaultConsumer(receiveChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                Runnable messageProcessorRunnable = () -> {
                    // parse broadcast message
                    JsonObject object = new JsonParser().parse(new String(body)).getAsJsonObject();
                    processMessage(object);
                };
                
                Workers.getInstance().submit(messageProcessorRunnable);
            }
        };

        // attach the consumer
        receiveChannel.basicConsume(bindingQueue, true, consumer);
	}
	
	public void broadcastRegisterNewUser(String userId) throws IOException{
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", "CONNECT");
		jsonObject.addProperty("userId", userId);
		jsonObject.addProperty("serverQueueName", configuration.getAppConfigProp("rabbitmq.queue.broadcast"));
        sendChannel.basicPublish(EXCHANGE_NAME, "", null, jsonObject.toString().getBytes());
	}
	
	public void broadcastUnregisterUser(String userId) throws IOException{
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", "DISCONNECT");
		jsonObject.addProperty("userId", userId);
        sendChannel.basicPublish(EXCHANGE_NAME, "", null, jsonObject.toString().getBytes());
	}
	
    public void closeConnection() throws IOException, TimeoutException {
        sendChannel.close();
        receiveChannel.close();
        connection.close();
    }
	
	private void processMessage(JsonObject jsonObject){
		String type = jsonObject.get("type").getAsString();
		String userId = jsonObject.get("userId").getAsString();
		
		if(type.equals("CONNECT")){
			String serverQueueName = jsonObject.get("serverQueueName").getAsString();
			realtimeDataHandler.registerExternalUser(userId, serverQueueName);
			return;
		}
		
		if(type.equals("DISCONNECT")){
			realtimeDataHandler.unRegisterExternalUser(userId);
			return;
		}
		
		throw new IllegalArgumentException("Invalid message type : " + type);
	}
}
