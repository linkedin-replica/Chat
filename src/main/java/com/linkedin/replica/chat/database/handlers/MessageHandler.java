package com.linkedin.replica.chat.database.handlers;

import java.util.ArrayList;
import java.util.List;

import com.linkedin.replica.chat.models.Message;

public interface MessageHandler extends DatabaseHandler{

	public void insertMessages(ArrayList<Message> msg);

	public Message getMessage(String messageId);

	public Message getLatestMessage();

}
