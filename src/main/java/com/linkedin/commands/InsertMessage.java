package com.linkedin.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.linkedIn.chat.chatinterface.ArangoChatHandler;
import com.linkedIn.chat.models.Message;

public class InsertMessage extends Command{
	int numberOfMessagesInQueue = 0;
	ArrayList<Message> messagesQueue = new ArrayList<Message>();
	
	public InsertMessage(HashMap<String, String> hMap) {
		super(hMap);
	}

	public static void main(String[] args) {
		
	}

	@Override
	public String execute()  {
		try {
			ArangoChatHandler aranagoChatHandler = new ArangoChatHandler();
	
			//Adding messages to queue
			if(numberOfMessagesInQueue < 20) {
				Message msg = new Message(hMap.get("from"), hMap.get("to"), new Date(hMap.get("date")), false, null, hMap.get("message"));
				messagesQueue.add(msg);
				numberOfMessagesInQueue++;
			}

			//Queue full -> Add messages to database
			if(messagesQueue.size() == 20) {
				for (Message message : messagesQueue) {
					aranagoChatHandler.insertMessage(message);
				}
				messagesQueue=new ArrayList<Message>();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
