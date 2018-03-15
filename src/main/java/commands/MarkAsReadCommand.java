package commands;

import java.io.IOException;
import java.util.HashMap;

import database.handlers.impl.ArangoChatHandler;

public class MarkAsReadCommand extends Command {

	public MarkAsReadCommand(HashMap<String, String> hMap) {
		super(hMap);
	}

	@Override
	public String execute() throws IOException{
		String messageId = hMap.get("messageId");
		ArangoChatHandler ac = new ArangoChatHandler();
		ac.markAsRead(messageId);
		return null;
	}

}
