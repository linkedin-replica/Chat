package com.linkedin.replica.chat.realtime.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.linkedin.replica.chat.models.Message;
import com.linkedin.replica.chat.realtime.ChatObject;
import com.linkedin.replica.chat.realtime.RealtimeDataHandler;
import com.linkedin.replica.chat.utils.JwtUtilities;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.UUID;

public class ChatDataListener implements DataListener<ChatObject> {
    private RealtimeDataHandler realtimeDataHandler = RealtimeDataHandler.getInstance();
    private SocketIOServer server;

    public ChatDataListener(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) throws Exception {
        String token = data.getThreadToken();
        Jws<Claims> claims = JwtUtilities.getClaims(token);
        if(claims == null) {
            server.getClient(client.getSessionId()).sendEvent("errorevent", "Unauthorized request.");
            return;
        }

        String senderId = claims.getBody().get("senderId").toString();
        String receiverId = claims.getBody().get("receiverId").toString();

        Message message = new Message(senderId, receiverId, System.currentTimeMillis(), data.getMessage());
        realtimeDataHandler.sendMessage(message);
        client.sendEvent("chatevent", message);
    }
}
