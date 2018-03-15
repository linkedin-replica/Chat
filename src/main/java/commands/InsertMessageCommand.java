package commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import database.handlers.impl.ArangoChatHandler;
import models.Command;
import models.Message;

public class InsertMessageCommand extends Command {
	int numberOfMessages = 0;
	ArrayList<Message> allMessages = new ArrayList<Message>();

	public InsertMessageCommand(HashMap<String, String> hMap) {
		super(hMap);
	}

	@Override
	public String execute() {
		try {
			ArangoChatHandler ac = new ArangoChatHandler();

			if (numberOfMessages < 20) {
				Message msg = new Message(hMap.get("from"), hMap.get("to"), new Date(hMap.get("date")), false,
						hMap.get("message"));
				allMessages.add(msg);
				numberOfMessages++;
			}

			if (allMessages.size() == 20) {
				for (Message message : allMessages) {

					ac.insertMessage(message);
				}

				allMessages = new ArrayList<Message>();
			}

		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
