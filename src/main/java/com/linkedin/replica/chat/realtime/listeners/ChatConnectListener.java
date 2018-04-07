package com.linkedin.replica.chat.realtime.listeners;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.google.gson.JsonObject;
import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.messaging.BroadcastChannels;
import com.linkedin.replica.chat.realtime.RealtimeDataHandler;
import com.linkedin.replica.chat.utils.JwtUtilities;
import com.rabbitmq.client.Channel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class ChatConnectListener implements ConnectListener{
    private RealtimeDataHandler realtimeDataHandler = RealtimeDataHandler.getInstance();
    private final String EXCHANGE_NAME = Configuration.getInstance().getAppConfigProp("rabbitmq.queue.broadcast");

    @Override
    public void onConnect(SocketIOClient socketIOClient) {
        String token = socketIOClient.getHandshakeData().getSingleUrlParam("threadToken");
        Jws<Claims> claims = JwtUtilities.getClaims(token);

        if(claims == null)
            return;

        String senderId = claims.getBody().get("senderId").toString();
        System.out.println("User " + senderId + " connected successfully");

        try {
			realtimeDataHandler.connectUser(senderId, socketIOClient.getSessionId().toString());
			broadcastRegisterNewUser(senderId);
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
    }
    
	private void broadcastRegisterNewUser(String userId) throws IOException, TimeoutException{
		Channel sendChannel = BroadcastChannels.getInstance().sendBroadcastChannel();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", "CONNECT");
		jsonObject.addProperty("userId", userId);
		jsonObject.addProperty("serverQueueName", Configuration.getInstance().getAppConfigProp("rabbitmq.queue.broadcast"));
        sendChannel.basicPublish(EXCHANGE_NAME, "", null, jsonObject.toString().getBytes());
	}
}
