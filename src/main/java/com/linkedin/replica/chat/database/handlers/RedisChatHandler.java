package com.linkedin.replica.chat.database.handlers;

import java.util.List;

import com.linkedin.replica.chat.models.Message;

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
