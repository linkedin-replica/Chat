package com.linkedin.replica.chat.commands.impl;

import java.io.IOException;
import java.util.HashMap;

import com.linkedin.replica.chat.commands.Command;
import com.linkedin.replica.chat.database.handlers.ChatHandler;

public class GetHistoryCommand extends Command {

	public GetHistoryCommand(HashMap<String, Object> args) {
		super(args);
	}


	@Override
	public String execute(){
		validateArgs(new String[]{"userId1", "userId2"});

		String userId1 = (String) args.get("userId1");
		String userId2 = (String)args.get("userId2");
		String limit = (String)args.get("limit");
		String offset = (String)args.get("offset");

		ChatHandler chatHandler = (ChatHandler) this.dbHandler;

		if ((limit.equals(null) && offset.equals(null) || (limit.isEmpty() && offset.isEmpty()))) {
			chatHandler.getChatHistory(userId1, userId2);
		} else {
			chatHandler.getChatHistory(userId1, userId2, Integer.parseInt(offset), Integer.parseInt(limit));
		}

		return null;
	}

}
