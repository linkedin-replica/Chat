package com.linkedin.replica.chat.realtime;

import com.corundumstudio.socketio.SocketIOServer;
import com.linkedin.replica.chat.messaging.BroadcastMessageHandler;
import com.linkedin.replica.chat.messaging.InterChatServersMessageHandler;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

public class RealtimeDataHandler {
    private ConcurrentHashMap<String, String> idToSessionMap, sessionToIdMap;
    private ConcurrentHashMap<String, String> externalOnlineUsersMap;
    private InterChatServersMessageHandler interChatServersMessageHandler;
    private SocketIOServer server;
    private static RealtimeDataHandler instance;

    private RealtimeDataHandler(SocketIOServer server) throws IOException, TimeoutException {
        this.server = server;
        idToSessionMap = new ConcurrentHashMap<>();
        sessionToIdMap = new ConcurrentHashMap<>();
        externalOnlineUsersMap = new ConcurrentHashMap<>();
        interChatServersMessageHandler = new InterChatServersMessageHandler();
    }


    public static void init(SocketIOServer server) throws IOException, TimeoutException {
        instance = new RealtimeDataHandler(server);
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

    public void sendMessage(String senderId, String receiverId, String message) throws IOException {
        if(isUserConnectedHere(receiverId)) {
            server.getClient(UUID.fromString(idToSessionMap.get(receiverId)))
                    .sendEvent("chatevent", message);
        }
        else if(externalOnlineUsersMap.containsKey(receiverId)) {
            interChatServersMessageHandler.sendMessage(senderId, receiverId, message, externalOnlineUsersMap.get(receiverId));
        }
        else {
            // user is offline; do nothing
        }
        // TODO append to buffer
    }
}
