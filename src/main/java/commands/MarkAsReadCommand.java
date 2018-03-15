package commands;

import java.io.IOException;
import java.util.HashMap;

import database.handlers.impl.ArangoChatHandler;
import models.Command;

public class MarkAsReadCommand extends Command {

	public MarkAsReadCommand(HashMap<String, String> hMap) {
		super(hMap);
	}

	@Override
	public String execute() {
		String messageId = hMap.get("messageId");

		try {
			ArangoChatHandler ac = new ArangoChatHandler();
			ac.markAsRead(messageId);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
