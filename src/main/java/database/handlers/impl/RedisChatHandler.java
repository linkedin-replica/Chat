package database.handlers.impl;

import java.util.List;

import database.handlers.ChatHandler;
import models.Message;

public class RedisChatHandler implements ChatHandler {

	public void insertMessage(Message msg) {
		// TODO Auto-generated method stub

	}

	public List<Message> getChatHistory(String userId1, String userId2) {
		// TODO Auto-generated method stub
		return null;
	}

	public void markAsRead(String messageId) {
		// TODO Auto-generated method stub

	}

	public List<Message> getChatHistory(String userId1, String userId2, int limit, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	public Message getMessage(String messageId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Message getLatestMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
