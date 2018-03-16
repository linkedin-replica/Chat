package commands;

import java.io.IOException;
import java.util.HashMap;

import database.handlers.ArangoChatHandler;
import database.handlers.ChatHandler;

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
