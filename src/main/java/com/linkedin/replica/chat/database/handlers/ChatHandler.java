package com.linkedin.replica.chat.database.handlers;

import java.util.ArrayList;
import java.util.List;

import com.linkedin.replica.chat.models.Message;

public interface ChatHandler extends DatabaseHandler{

	public void insertMessages(ArrayList<Message> msg);

	public List<Message> getChatHistory(String userId1, String userId2);

	public List<Message> getChatHistory(String userId1, String userId2, int limit, int offset);

	public Message getMessage(String messageId);

	public Message getLatestMessage();

}
