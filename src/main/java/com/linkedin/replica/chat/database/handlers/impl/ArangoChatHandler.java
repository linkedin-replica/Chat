package com.linkedin.replica.chat.database.handlers.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;

import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.database.DatabaseConnection;
import com.linkedin.replica.chat.database.handlers.ChatHandler;
import com.linkedin.replica.chat.models.Message;

public class ArangoChatHandler implements ChatHandler {
	private ArangoDatabase dbInstance;
	private ArangoCollection collection;
	private String collectionName;
	private Configuration config;
	private ArangoDB arangoDriver;

	public ArangoChatHandler() {
		config = Configuration.getInstance();
		arangoDriver = DatabaseConnection.getInstance().getArangoDriver();

		collectionName = config.getArangoConfigProp("collection.messages.name");
		dbInstance = arangoDriver.db(config.getArangoConfigProp("db.name"));
		collection = dbInstance.collection(collectionName);
	}

	public void insertMessages(ArrayList<Message> msgs){
		collection.insertDocuments(msgs);
	}

	public Message getMessage(String messageId) {
		return collection.getDocument(messageId, Message.class);
	}

	public List<Message> getChatHistory(String userId1, String userId2) {

		String query = "For m in " + collectionName
				+ " FILTER (m.sender == @userId1 && m.receiver == @userId2)|| (m.sender == @userId2 && m.receiver == @userId1)  RETURN m";
		Map<String, Object> bindVars = new HashMap<String, Object>();
		bindVars.put("userId1", userId1);
		bindVars.put("userId2", userId2);

		// Process query
		ArangoCursor<Message> cursor = dbInstance.query(query, bindVars, null, Message.class);
		ArrayList<Message> result = new ArrayList<Message>();

		for (; cursor.hasNext();)
			result.add(cursor.next());
		return result;
	}


	public List<Message> getChatHistory(String userId1, String userId2, int offset, int limit) {
		String query = "For m in " + collectionName
				+ " FILTER (m.sender == @userId1 && m.receiver == @userId2)|| (m.sender == @userId2 && m.receiver == @userId1) LIMIT "
				+ offset + ", " + limit + " RETURN m";
		Map<String, Object> bindVars = new HashMap<String, Object>();
		bindVars.put("userId1", userId1);
		bindVars.put("userId2", userId2);

		// process query
		ArangoCursor<Message> cursor = dbInstance.query(query, bindVars, null, Message.class);

		ArrayList<Message> result = new ArrayList<Message>();
		for (; cursor.hasNext();)
			result.add(cursor.next());
		return result;
	}

	public Message getLatestMessage() {
		String query = "For m in " + collectionName + " Sort m._key desc RETURN m";

		// process query
		ArangoCursor<Message> cursor = dbInstance.query(query, null, null, Message.class);
		Message result = cursor.next();
		return result;
	}

}
