package com.linkedin.replica.chat.realtime;

import com.corundumstudio.socketio.SocketIOServer;
import com.linkedin.replica.chat.messaging.InterChatServersMessageHandler;
import com.linkedin.replica.chat.models.Message;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

public class RealtimeDataHandler {
	private ConcurrentHashMap<String, String> idToSessionMap, sessionToIdMap;
	private ConcurrentHashMap<String, String> externalOnlineUsersMap;
	private InterChatServersMessageHandler interChatServersMessageHandler;
	private SocketIOServer server;

	private static RealtimeDataHandler instance;
	private static BufferWatcher bufferWatcher;
	private final BlockingQueue<Message> consumerQueue;

	private RealtimeDataHandler(SocketIOServer server) throws IOException,
			TimeoutException {
		this.server = server;
		idToSessionMap = new ConcurrentHashMap<>();
		sessionToIdMap = new ConcurrentHashMap<>();
		externalOnlineUsersMap = new ConcurrentHashMap<>();
		consumerQueue = new LinkedBlockingQueue<>();

		interChatServersMessageHandler = new InterChatServersMessageHandler();
		bufferWatcher = new BufferWatcher(consumerQueue);

	}

	public static void init(SocketIOServer server) throws IOException,
			TimeoutException {
		if (instance == null) {
			instance = new RealtimeDataHandler(server);
			new Thread(() -> {
				bufferWatcher.startTimer(); // start timer thread
				bufferWatcher.run();
			}).start();
		}
	}

	public static RealtimeDataHandler getInstance() {
		return instance;
	}

	public void registerExternalUser(String userId, String serverQueueName) {
		externalOnlineUsersMap.put(userId, serverQueueName);
	}

	public void unRegisterExternalUser(String userId) {
		externalOnlineUsersMap.remove(userId);
	}

	public void connectUser(String userId, String sessionId) throws IOException {
		if (idToSessionMap.containsKey(userId))
			idToSessionMap.remove(userId);

		if (sessionToIdMap.containsKey(sessionId))
			sessionToIdMap.remove(sessionId);

		idToSessionMap.put(userId, sessionId);
		sessionToIdMap.put(sessionId, userId);
	}

	public void disconnectUser(String sessionId) throws IOException {
		if (sessionToIdMap.containsKey(sessionId)) {
			String userId = sessionToIdMap.remove(sessionId);
			if (idToSessionMap.containsKey(userId))
				idToSessionMap.remove(userId);
		}
	}

	private boolean isUserConnectedHere(String userId) {
		return idToSessionMap.containsKey(userId);
	}

	public String getUserId(String sessionId) {
		return sessionToIdMap.get(sessionId);
	}

	public void sendMessage(Message message) throws IOException {
		if (isUserConnectedHere(message.getReceiverId())) {
			server.getClient(UUID.fromString(idToSessionMap.get(message.getReceiverId()))).sendEvent("chatevent", message);
			consumerQueue.add(message); // add new message to queue

		} else if (externalOnlineUsersMap.containsKey(message.getReceiverId())) {
			interChatServersMessageHandler.sendMessage(message, externalOnlineUsersMap.get(message.getReceiverId()));

		} else { // receiver is offline
			consumerQueue.add(message); // add new message to queue
		}
	}
}
