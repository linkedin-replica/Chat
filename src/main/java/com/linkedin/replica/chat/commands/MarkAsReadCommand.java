package com.linkedin.replica.chat.commands;

import java.io.IOException;
import java.util.HashMap;

import com.linkedin.replica.chat.database.handlers.ArangoChatHandler;
import com.linkedin.replica.chat.database.handlers.ChatHandler;

public class MarkAsReadCommand extends Command {

	public MarkAsReadCommand(HashMap<String, String> args) {
		super(args);
	}

	@Override
	public String execute() throws IOException{
		String messageId = args.get("messageId");
        ChatHandler chatHandler = (ChatHandler) this.dbHandler;
       
        //TODO Validate args from chatHandler
		
		chatHandler.markAsRead(messageId);
		return null;
	}

}
