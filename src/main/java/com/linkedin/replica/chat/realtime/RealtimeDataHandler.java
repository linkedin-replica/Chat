package com.linkedin.replica.chat.realtime;

import com.corundumstudio.socketio.SocketIOServer;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RealtimeDataHandler {
    private ConcurrentHashMap<String, String> idToSessionMap, sessionToIdMap;
    private static RealtimeDataHandler instance;

    private RealtimeDataHandler() {
        idToSessionMap = new ConcurrentHashMap<>();
        sessionToIdMap = new ConcurrentHashMap<>();
    }

    public static void init() {
        instance = new RealtimeDataHandler();
    }

    public static RealtimeDataHandler getInstance() {
        return instance;
    }


    public void connectUser(String userId, String sessionId) {
        if(idToSessionMap.containsKey(userId))
            idToSessionMap.remove(userId);
        if(sessionToIdMap.containsKey(sessionId))
            sessionToIdMap.remove(sessionId);
        idToSessionMap.put(userId, sessionId);
        sessionToIdMap.put(sessionId, userId);
    }

    public void disconnectUser(String sessionId) {
        if(sessionToIdMap.containsKey(sessionId)) {
            String userId = sessionToIdMap.remove(sessionId);
            if(idToSessionMap.containsKey(userId))
                idToSessionMap.remove(userId);
        }
    }

    private boolean isUserConnectedHere(String userId) {
        return idToSessionMap.containsKey(userId);
    }

    public void sendMessage(SocketIOServer server, String senderId, String receiverId, ChatObject data) {
        if(isUserConnectedHere(receiverId)) {
            server.getClient(UUID.fromString(idToSessionMap.get(receiverId)))
                    .sendEvent("chatevent", data.getMessage());
        }
        else
            System.out.println("User " + receiverId + " is offline.");
        // TODO check other servers

        // TODO append to buffer
    }
}
