package database.handlers.impl;

import java.util.List;

import database.handlers.ChatHandler;
import models.Message;

public class RedisChatHandler implements ChatHandler {

	public void insertMessage(Message msg) {

	}

	public List<Message> getChatHistory(String userId1, String userId2) {
		return null;
	}

	public void markAsRead(String messageId) {
		
	}

	public List<Message> getChatHistory(String userId1, String userId2, int limit, int offset) {
		return null;
	}

	public Message getMessage(String messageId) {
		return null;
	}

	public Message getLatestMessage() {
		return null;
	}

}
