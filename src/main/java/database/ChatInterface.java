package database;

import java.util.List;

import models.Message;

public interface ChatInterface {

	public void insertMessage(models.Message msg);

	public List<Message> getChatHistory(String userId1, String userId2);

	public List<Message> getChatHistory(String userId1, String userId2, int limit, int offset);

	public void markAsRead(String messageId);

	public Message getMessage(String messageId);

	public Message getLatestMessage();

}
