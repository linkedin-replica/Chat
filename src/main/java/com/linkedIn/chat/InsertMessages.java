package com.linkedIn.chat;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.linkedIn.chat.config.DatabaseConnection;
import com.linkedIn.chat.models.Message;

import redis.clients.jedis.Jedis;

public class InsertMessages extends Command{
	int numberOfMessages=0;
	public InsertMessages(HashMap<String, String> hMap) {
		super(hMap);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public String execute()  {
		// TODO Auto-generated method stub
	
		try {
			//insert
			//change keys to match collection
			
			ArangoDB aDB= DatabaseConnection.getInstance().getArangodb();
			
			ArangoCollection m=aDB.db().collection("messages");
			ArrayList<Message> allMessages=new ArrayList<Message>();
			
			while(numberOfMessages<20) {
				Message msg=new Message(hMap.get("from"), hMap.get("to"), new Date(hMap.get("date")), false, null, hMap.get("message"));
				allMessages.add(msg);
				numberOfMessages++;
				
			}
			if(numberOfMessages==19) {
								for (Message message : allMessages) {
					JsonObject value=Json.createObjectBuilder()
				            .add("message", message.getMsg())
				            .add("sender", message.getSentFrom())
				            .add("receiver",message.getSentTo())
				            .add("sent_date", message.getSentDate().toString())
				            .add("read", message.isRead())
				            .add("read_date", message.getReadDate().toString())
				            .build();
							m.insertDocument(value);
				}
					
				
			}
			
			//TODO
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//add message to database
		
		
		return null;
	}

}
