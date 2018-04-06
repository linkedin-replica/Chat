package com.linkedin.replica.chat.commands;

import java.io.IOException;
import java.util.HashMap;

import com.linkedin.replica.chat.database.handlers.ArangoChatHandler;
import com.linkedin.replica.chat.database.handlers.ChatHandler;

public class GetHistoryCommand extends Command {

	public GetHistoryCommand(HashMap<String, String> args) {
		super(args);
	}

	public static void main(String[] args) {

	}

	@Override
	public String execute() throws IOException{
		String userId1 = args.get("userId1");
		String userId2 = args.get("userId2");
		String limit = args.get("limit");
		String offset = args.get("offset");

		ChatHandler chatHandler = (ChatHandler) this.dbHandler;
        //TODO Validate args from chatHandler
		
		if ((limit.equals(null) && offset.equals(null) || (limit.isEmpty() && offset.isEmpty()))) {
			chatHandler.getChatHistory(userId1, userId2);
		} else {
			chatHandler.getChatHistory(userId1, userId2, Integer.parseInt(offset), Integer.parseInt(limit));
		}

		return null;
	}

}
