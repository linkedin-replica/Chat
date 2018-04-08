package com.linkedin.replica.chat.messaging;

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
    private Channel receiveChannel;
    private Channel sendChannel;

    public InterChatServersMessageHandler() throws IOException, TimeoutException {
        // get channels
        sendChannel = RabbitMQChannels.getInstance().sendInterChannel();
        receiveChannel = RabbitMQChannels.getInstance().receiveInterChannel();
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

        sendChannel.basicPublish("", queueName, null, object.toString().getBytes());
    }

}
