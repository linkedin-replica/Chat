package com.linkedin.replica.chat.commands.impl;

import java.util.ArrayList;
import java.util.HashMap;

import com.linkedin.replica.chat.commands.Command;
import com.linkedin.replica.chat.database.handlers.MessageHandler;
import com.linkedin.replica.chat.models.Message;

public class InsertBufferedMessagesCommand extends Command{

	public InsertBufferedMessagesCommand(HashMap<String, Object> args) {
		super(args);
	}

	@Override
	public String execute(){
		MessageHandler chatHandler = (MessageHandler) this.dbHandler;
		chatHandler.insertMessages((ArrayList<Message>) args.get("messages"));
		return null;
	}

}
