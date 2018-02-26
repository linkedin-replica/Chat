package com.linkedIn.chat;

import java.io.IOException;
import java.util.HashMap;

import com.linkedIn.chat.chatinterface.ArangoChatHandler;

public class GetHistory extends Command{

	public GetHistory(HashMap<String, String> hMap) {
		super(hMap);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public String execute() {
		// TODO Auto-generated method stub
		 String userId1 = hMap.get("userId1");
		 String userId2 = hMap.get("userId2");
		
		 try {
			ArangoChatHandler ac=new ArangoChatHandler();
			ac.getChatHistory(userId1, userId2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}

}
