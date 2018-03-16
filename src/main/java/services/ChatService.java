package services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import commands.Command;
import config.ConfigReader;
import database.handlers.ChatHandler;

/**
 * Chat Service is responsible for taking input from controller, reading commands config file to 
 * get specific command responsible for handling input request and also get DatabaseHandler name
 * Associated with this command 
 * 
 * It will call command execute method after passing to its DatabaseHandler
 */

public class ChatService {
    private ConfigReader config;

	
	public ChatService() throws FileNotFoundException, IOException{
        config = ConfigReader.getInstance();
	}
		
	public Object serve(String commandName, HashMap<String, String> args) throws Exception {

        Class<?> dbHandlerClass = ConfigReader.getHandlerClass(commandName);
        ChatHandler dbHandler = (ChatHandler) dbHandlerClass.newInstance();

        Class<?> commandClass = config.getCommandClass(commandName);
        Constructor<?> constructor = commandClass.getConstructor(new Class<?>[]{HashMap.class, ChatHandler.class});
        Command command = (Command) constructor.newInstance(args,dbHandler);

        return command.execute();
	}
}
