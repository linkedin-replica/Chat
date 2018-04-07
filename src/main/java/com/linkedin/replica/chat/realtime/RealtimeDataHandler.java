package com.linkedin.replica.chat.realtime;

import com.corundumstudio.socketio.SocketIOServer;
import com.linkedin.replica.chat.messaging.BroadcastMessageHandler;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

public class RealtimeDataHandler {
    private ConcurrentHashMap<String, String> idToSessionMap, sessionToIdMap;
    private ConcurrentHashMap<String, String> externalOnlineUsersMap;
    private static RealtimeDataHandler instance;
    
    private RealtimeDataHandler() throws IOException, TimeoutException {
        idToSessionMap = new ConcurrentHashMap<String, String>();
        sessionToIdMap = new ConcurrentHashMap<String, String>();
        externalOnlineUsersMap = new ConcurrentHashMap<String, String>();
    }

    public static void init() throws IOException, TimeoutException {
        instance = new RealtimeDataHandler();
    }

    public static RealtimeDataHandler getInstance() {
        return instance;
    }

    public void registerExternalUser(String userId, String serverQueueName){
    	externalOnlineUsersMap.put(userId, serverQueueName);
    }
    
    public void unRegisterExternalUser(String userId){
    	externalOnlineUsersMap.remove(userId);
    }
    
    public void connectUser(String userId, String sessionId) throws IOException {
        if(idToSessionMap.containsKey(userId))
            idToSessionMap.remove(userId);
        if(sessionToIdMap.containsKey(sessionId))
            sessionToIdMap.remove(sessionId);
        idToSessionMap.put(userId, sessionId);
        sessionToIdMap.put(sessionId, userId);        
    }

    public void disconnectUser(String sessionId) throws IOException {
        if(sessionToIdMap.containsKey(sessionId)) {
            String userId = sessionToIdMap.remove(sessionId);
            if(idToSessionMap.containsKey(userId))
                idToSessionMap.remove(userId);            
        }
    }

    private boolean isUserConnectedHere(String userId) {
        return idToSessionMap.containsKey(userId);
    }
    
    public String getUserId(String sessionId){
    	return sessionToIdMap.get(sessionId);
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
