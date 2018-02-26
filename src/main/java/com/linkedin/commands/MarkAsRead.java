package com.linkedin.commands;

import java.io.IOException;
import java.util.HashMap;

import com.linkedIn.chat.chatinterface.ArangoChatHandler;

public class MarkAsRead extends Command{

	public MarkAsRead(HashMap<String, String> hMap) {
		super(hMap);
	}

	public static void main(String[] args) {
		
	}

	@Override
	public String execute() {
		
		String messageId = hMap.get("messageId");
		String readDate = hMap.get("read_date");
	    
	    try {
			ArangoChatHandler aranagoChatHandler = new ArangoChatHandler();
			aranagoChatHandler.markAsRead(messageId,readDate);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
