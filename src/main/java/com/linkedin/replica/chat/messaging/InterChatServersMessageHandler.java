package com.linkedin.replica.chat.messaging;

import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.realtime.RealtimeDataHandler;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class InterChatServersMessageHandler {
    private final Configuration configuration = Configuration.getInstance();

    private final String QUEUE_NAME = configuration.getAppConfigProp("rabbitmq.queue.inter");
    private final String RABBIT_MQ_IP = configuration.getAppConfigProp("rabbitmq.ip");
    private final String RABBIT_MQ_USERNAME = configuration.getAppConfigProp("rabbitmq.username");
    private final String RABBIT_MQ_PASSWORD = configuration.getAppConfigProp("rabbitmq.password");

    private ConnectionFactory factory;
    private Channel receiveChannel;
    private Channel sendChannel;
    private Connection connection;

    public InterChatServersMessageHandler() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setUsername(RABBIT_MQ_USERNAME);
        factory.setPassword(RABBIT_MQ_PASSWORD);
        factory.setHost(RABBIT_MQ_IP);
        connection = factory.newConnection();

        // create channels
        sendChannel = connection.createChannel();
        receiveChannel = connection.createChannel();
        initConsumer();
    }

    private void initConsumer() throws IOException{
        receiveChannel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // Create the consumer (listener) for the new messages
        Consumer consumer = new DefaultConsumer(receiveChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                JsonObject object = new JsonParser().parse(new String(body)).getAsJsonObject();
                System.out.println("Handle deliver of entra " + object);
                String senderId = object.get("senderId").getAsString();
                String receiverId = object.get("receiverId").getAsString();
                String message = object.get("message").getAsString();

                RealtimeDataHandler.getInstance().sendMessage(senderId, receiverId, message);
            }
        };

        // attach the consumer
        receiveChannel.basicConsume(QUEUE_NAME, true, consumer);
    }

    public void sendMessage(String senderId, String receiverId, String message, String queueName) throws IOException {
        JsonObject object = new JsonObject();
        object.addProperty("senderId", senderId);
        object.addProperty("receiverId", receiverId);
        object.addProperty("message", message);

        System.out.println("Publishing to " + queueName);
        sendChannel.basicPublish("", queueName, null, object.toString().getBytes());
    }

}
