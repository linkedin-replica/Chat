package commands;

import java.io.IOException;
import java.util.HashMap;

import database.handlers.impl.ArangoChatHandler;
import models.Command;

public class GetHistoryCommand extends Command {

	public GetHistoryCommand(HashMap<String, String> hMap) {
		super(hMap);
	}

	public static void main(String[] args) {

	}

	@Override
	public String execute() {
		String userId1 = hMap.get("userId1");
		String userId2 = hMap.get("userId2");
		String limit = hMap.get("limit");
		String offset = hMap.get("offset");

		try {
			ArangoChatHandler ac = new database.handlers.impl.ArangoChatHandler();
			if ((limit.equals(null) && offset.equals(null) || (limit.isEmpty() && offset.isEmpty()))) {
				ac.getChatHistory(userId1, userId2);
			} else {
				ac.getChatHistory(userId1, userId2, Integer.parseInt(offset), Integer.parseInt(limit));
			}

		} 
		catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
