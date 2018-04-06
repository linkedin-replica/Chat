package com.linkedin.replica.chat.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.linkedin.replica.chat.database.handlers.ArangoChatHandler;
import com.linkedin.replica.chat.database.handlers.ChatHandler;
import com.linkedin.replica.chat.models.Message;

public class InsertMessageCommand extends Command {
	
	int numberOfMessages = 0;
	ArrayList<Message> allMessages = new ArrayList<Message>();

	public InsertMessageCommand(HashMap<String, String> args) {
		super(args);
	}

	@Override
	public String execute() throws IOException{
	
		ChatHandler chatHandler = (ChatHandler) this.dbHandler;
        //TODO Validate args from chatHandler

		if (numberOfMessages < 20) {
			Message msg = new Message();
			// args.get("from"), args.get("to"), new Date(args.get("date")), false,
			//		args.get("message"));
			allMessages.add(msg);
			numberOfMessages++;
		}

		if (allMessages.size() == 20) {
			for (Message message : allMessages) {
				chatHandler.insertMessage(message);
			}
			allMessages = new ArrayList<Message>();
		}

		return null;
	}
}
