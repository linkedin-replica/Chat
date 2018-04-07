package com.linkedin.replica.chat.realtime.listeners;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.google.gson.JsonObject;
import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.messaging.BroadcastChannels;
import com.linkedin.replica.chat.realtime.RealtimeDataHandler;
import com.rabbitmq.client.Channel;

public class ChatDisconnectListener implements DisconnectListener{
    private RealtimeDataHandler realtimeDataHandler = RealtimeDataHandler.getInstance();
    private final String EXCHANGE_NAME = Configuration.getInstance().getAppConfigProp("rabbitmq.queue.broadcast");

    @Override
    public void onDisconnect(SocketIOClient socketIOClient) {
        try {
        	String sessionId = socketIOClient.getSessionId().toString();
			realtimeDataHandler.disconnectUser(sessionId);
	        String userId = RealtimeDataHandler.getInstance().getUserId(sessionId);
	        broadcastUnregisterUser(userId);
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
    }
    
	private void broadcastUnregisterUser(String userId) throws IOException, TimeoutException{
		Channel sendChannel = BroadcastChannels.getInstance().sendBroadcastChannel();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", "DISCONNECT");
		jsonObject.addProperty("userId", userId);
        sendChannel.basicPublish(EXCHANGE_NAME, "", null, jsonObject.toString().getBytes());
	}
}
