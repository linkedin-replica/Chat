package commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import javax.json.JsonObject;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;

import database.handlers.impl.ArangoChatHandler;
import models.Command;

public class MarkAsReadCommand extends Command {

	public MarkAsReadCommand(HashMap<String, String> hMap) {
		super(hMap);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public String execute() {
		// TODO Auto-generated method stub
		String messageId = hMap.get("messageId");

		try {
			ArangoChatHandler ac = new ArangoChatHandler();
			ac.markAsRead(messageId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
