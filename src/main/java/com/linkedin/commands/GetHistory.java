package com.linkedin.commands;

import java.io.IOException;
import java.util.HashMap;

import com.linkedIn.chat.chatinterface.ArangoChatHandler;

public class GetHistory extends Command{

	public GetHistory(HashMap<String, String> hMap) {
		super(hMap);
	}

	public static void main(String[] args) {
		
	}

	@Override
	public String execute() {
		 String userId1 = hMap.get("userId1");
		 String userId2 = hMap.get("userId2");
		 String limit = hMap.get("limit");
		 String offset = hMap.get("offset");
		
		 try {
			 ArangoChatHandler arangoChatHandler = new ArangoChatHandler();
			
			 if((limit.equals(null) && offset.equals(null) || (limit.isEmpty() && offset.isEmpty())))
				 arangoChatHandler.getChatHistory(userId1, userId2);
			 else 
				 arangoChatHandler.getChatHistory(userId1, userId2,Integer.parseInt(limit),Integer.parseInt(offset));
			 
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}

}
