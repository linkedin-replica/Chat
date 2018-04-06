package com.linkedin.replica.chat.commands.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.linkedin.replica.chat.commands.Command;
import com.linkedin.replica.chat.database.handlers.ChatHandler;
import com.linkedin.replica.chat.models.Message;

public class InsertMessageCommand extends Command {

	public InsertMessageCommand(HashMap<String, Object> args) {
		super(args);
	}

	@Override
	public String execute(){

		ChatHandler chatHandler = (ChatHandler) this.dbHandler;
		validateArgs(new String[]{"messages"});

		chatHandler.insertMessages((ArrayList<Message>) args.get("messages"));

		return null;
	}
}
