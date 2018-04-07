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
import com.linkedin.replica.chat.database.handlers.MessageHandler;
import com.linkedin.replica.chat.models.Message;

public class ArangoMessageHandler implements MessageHandler {
	private ArangoDatabase dbInstance;
	private ArangoCollection collection;
	private String collectionName;
	private Configuration config;
	private ArangoDB arangoDriver;

	public ArangoMessageHandler() {
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
	
	public Message getLatestMessage() {
		String query = "For m in " + collectionName + " Sort m._key desc RETURN m";

		// process query
		ArangoCursor<Message> cursor = dbInstance.query(query, null, null, Message.class);
		Message result = cursor.next();
		return result;
	}
}
