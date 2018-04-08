package com.linkedin.replica.chat.messaging;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.realtime.RealtimeDataHandler;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class BroadcastMessageHandler {
	private final Configuration configuration = Configuration.getInstance();
    private final String EXCHANGE_NAME = configuration.getAppConfigProp("rabbitmq.queue.broadcast");
    private Channel receiveChannel;

    public BroadcastMessageHandler() throws IOException, TimeoutException{        
        //get receive channel
        receiveChannel = RabbitMQChannels.getInstance().receiveBroadcastChannel();
        initConsumer();
    }
	
	private void initConsumer() throws IOException{
		String bindingQueue = receiveChannel.queueDeclare().getQueue();
		receiveChannel.queueBind(bindingQueue, EXCHANGE_NAME, "");
		
        // Create the consumer (listener) for the new messages
        Consumer consumer = new DefaultConsumer(receiveChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                JsonObject object = new JsonParser().parse(new String(body)).getAsJsonObject();
                processMessage(object);
            }
        };

        // attach the consumer
        receiveChannel.basicConsume(bindingQueue, true, consumer);
	}

    public void closeConnection() throws IOException, TimeoutException {
    	RabbitMQChannels.getInstance().closeConnections();
    }
	
	private void processMessage(JsonObject jsonObject){
		String type = jsonObject.get("type").getAsString();
		String userId = jsonObject.get("userId").getAsString();

        RealtimeDataHandler realtimeDataHandler = RealtimeDataHandler.getInstance();
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
