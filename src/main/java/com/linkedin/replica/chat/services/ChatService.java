package com.linkedin.replica.chat.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import com.linkedin.replica.chat.commands.Command;
import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.database.handlers.ChatHandler;
import com.linkedin.replica.chat.database.handlers.DatabaseHandler;

/**
 * Chat Service is responsible for taking input from com.linkedin.replica.chat.controller, reading com.linkedin.replica.chat.commands com.linkedin.replica.chat.config file to
 * get specific command responsible for handling input request and also get DatabaseHandler name
 * Associated with this command 
 * 
 * It will call command execute method after passing to its DatabaseHandler
 */

public class ChatService {
    private Configuration config;

	
	public ChatService() {
        config = Configuration.getInstance();
	}
		
	public Object serve(String commandName, HashMap<String, Object> args) throws Exception {

        Class<?> commandClass = config.getCommandClass(commandName);
        Constructor constructor = commandClass.getConstructor(HashMap.class);
        Command command = (Command) constructor.newInstance(args);

        Class<?> dbHandlerClass = config.getDatabaseHandlerClass(commandName);
        DatabaseHandler dbHandler = (DatabaseHandler) dbHandlerClass.newInstance();

        command.setDbHandler(dbHandler);

        return command.execute();
	}
}
