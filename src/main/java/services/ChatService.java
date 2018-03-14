package services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;



import database.ChatInterface;
import models.Command;
import utils.ConfigReader;

/**
 * Search Service is responsible for taking input from controller, reading commands config file to 
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
		
	public  Object serve(String commandName, HashMap<String, String> args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Class<?> dbHandlerClass = ConfigReader.getHandlerClass(commandName);
        ChatInterface dbHandler = (ChatInterface) dbHandlerClass.newInstance();

        Class<?> commandClass = config.getCommandClass(commandName);
        Constructor constructor = commandClass.getConstructor(new Class<?>[]{HashMap.class, ChatInterface.class});
        Command command = (Command) constructor.newInstance(args,dbHandler);

        return command.execute();
	}
}
