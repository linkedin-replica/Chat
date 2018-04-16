package com.linkedin.replica.chat.database.handlers.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.database.DatabaseConnection;
import com.linkedin.replica.chat.database.handlers.RegisterHandler;
import com.linkedin.replica.chat.models.Message;
import com.linkedin.replica.chat.models.RegisterSessionInfo;
import com.linkedin.replica.chat.utils.JwtUtilities;

public class SessionRegisterHandler implements RegisterHandler{
	private ArangoDatabase dbInstance;
	private String collectionName;
	private Configuration config;
	private ArangoDB arangoDriver;

	public SessionRegisterHandler() {
		config = Configuration.getInstance();
		arangoDriver = DatabaseConnection.getInstance().getArangoDriver();

		collectionName = config.getArangoConfigProp("collection.messages.name");
		dbInstance = arangoDriver.db(config.getArangoConfigProp("db.name"));
	}

	@Override
	public RegisterSessionInfo register(String senderId, String receiverId, String offset, String limit) {
		List<Message> history = getChatHistory(senderId, receiverId, offset, limit);
		return getSessionInfo(senderId, receiverId, history);
	}

    private RegisterSessionInfo getSessionInfo(String senderId, String receiverId, List<Message> history) {
        String ip = config.getAppConfigProp("chat.ip");
        String port = config.getAppConfigProp("chat.port");

        String token = JwtUtilities.generateToken(senderId, receiverId);
        
        return new RegisterSessionInfo(ip, port, token, history);
    }
	
	private List<Message> getChatHistory(String userId1, String userId2, String strOffset, String strLimit) {
		int offset, limit;
		
		// validate and set offset
		if(strOffset  == null || strOffset.trim().isEmpty())
			offset = Integer.parseInt(config.getAppConfigProp("history.offset.default"));
		else{
			try{
				offset = Integer.parseInt(strOffset);
			}catch(NumberFormatException ex){
				offset = Integer.parseInt(config.getAppConfigProp("history.offset.default"));
			}
		}
		
		// validate and set limit
		if(strLimit  == null || strLimit.trim().isEmpty())
			limit = Integer.parseInt(config.getAppConfigProp("history.limit.default"));
		else{
			try{
				limit = Integer.parseInt(strLimit);
			}catch(NumberFormatException ex){
				limit = Integer.parseInt(config.getAppConfigProp("history.limit.default"));
			}
		}
		
			
		String query = "For m in " + collectionName
				+ " FILTER (m.senderId == @userId1 && m.receiverId == @userId2)|| (m.senderId == @userId2 && m.receiverId == @userId1) LIMIT "
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
}
