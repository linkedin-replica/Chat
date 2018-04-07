package com.linkedin.replica.chat.realtime.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.linkedin.replica.chat.realtime.ChatObject;
import com.linkedin.replica.chat.utils.JwtUtilities;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatDataListener implements DataListener<ChatObject> {
    private SocketIOServer server;
    private ConcurrentHashMap<String, String> idToSessionMap, sessionToIdMap;

    @Override
    public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) throws Exception {
        String token = data.getThreadToken();
        Jws<Claims> claims = JwtUtilities.getClaims(token);
        if(claims == null) {
            server.getClient(client.getSessionId()).sendEvent("errorevent", "Unauthorized request.");
            return;
        }

        String senderId = claims.getBody().getId();
        String receiverId = claims.getBody().get("receiverId").toString();
        System.out.println("Sender: " + senderId + ", receiver: " + receiverId);
        System.out.println("Message: " + data.getMessage());
        if(idToSessionMap.containsKey(receiverId)) {
            System.out.println("Sent message to receiver");
            server.getClient(UUID.fromString(idToSessionMap.get(receiverId))).sendEvent("chatevent", data);
        }
        else {
            System.out.println("> " + idToSessionMap);
            System.out.println("Receiver is offline");
        }

        // TODO append to buffer
    }
}
