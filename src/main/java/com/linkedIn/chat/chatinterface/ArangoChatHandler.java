package com.linkedIn.chat.chatinterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.linkedIn.chat.models.Message;
import com.linkedin.commands.ConfigReader;

public class ArangoChatHandler implements ChatInterface{
	 private ArangoDatabase dbInstance;
	 private ArangoCollection collection;
	 private String collectionName;
	 
	 public ArangoChatHandler() throws IOException {
    	 ConfigReader config = ConfigReader.getInstance();
         ArangoDB arangoDriver = DatabaseConnection.getDBConnection().getArangoDriver();
         collectionName = config.getArangoConfig("collection.messages.name");
         dbInstance = arangoDriver.db(config.getArangoConfig("db.name"));
         collection = dbInstance.collection(collectionName);
	 }
	 
	public void insertMessage(Message msg) {
		// TODO Auto-generated method stub
		collection.insertDocument(msg);
	}

	public List<Message>  getChatHistory(String userId1,String userId2) {
		
		// TODO Auto-generated method stub
		String query = "For m in " + collectionName + " FILTER m.userId1 == @userId1 && m.userId2 == @userId2  RETURN t";
		Map<String, Object> bindVars = new HashMap<String, Object>();
	    bindVars.put("userId1", userId1);
	    bindVars.put("userId2", userId2);

	    //Process query
        ArangoCursor<Message> cursor = dbInstance.query(query, bindVars, null, Message.class);
        ArrayList<Message> result = new ArrayList<Message>();
        
        for(; cursor.hasNext();)
            result.add(cursor.next());
        return result;
	}



	public void markAsRead(String messageId,String readDate) {
		// TODO Auto-generated method stub
		 // form the query
        String query = "FOR m in " + collectionName + " FILTER" +
                " m.messageId == @messageId &&" +
                " m.read == false" +
                " UPDATE { _key: m._key, read: true ,read_date: @readDate} IN " + collectionName;

        // bind the params
        Map<String, Object> bindVars = new HashMap<String, Object>();
        bindVars.put("messageId", messageId);

        // execute the query
        dbInstance.query(query, bindVars, null, null);
	}
	public List<Message> getChatHistory(String userId1, String userId2, int limit, int offset) {
		// TODO Auto-generated method stub
		String query = "For m in " + collectionName + " FILTER m.userId1 == @userId1 && m.userId2 == @userId2 +LIMIT " +limit +"OFSSET "+offset+ " RETURN t";
		  Map<String, Object> bindVars = new HashMap<String, Object>();
	        bindVars.put("userId1", userId1);
	        bindVars.put("userId2", userId2);

	        // process query
	        ArangoCursor<Message> cursor = dbInstance.query(query, bindVars, null, Message.class);

	        ArrayList<Message> result = new ArrayList<Message>();
	        for(; cursor.hasNext();)
	            result.add(cursor.next());
	        return result;
	}

}
