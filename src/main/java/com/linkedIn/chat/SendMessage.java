package com.linkedIn.chat;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.linkedIn.chat.config.DatabaseConnection;

import redis.clients.jedis.Jedis;

public class SendMessage extends Command{

	public SendMessage(HashMap<String, String> hMap) {
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
			JsonObject value=Json.createObjectBuilder()
            .add("message", hMap.get("message"))
            .add("sender", hMap.get("from"))
            .add("receiver",hMap.get("to"))
            .add("date", hMap.get("date"))
            .build();
			m.insertDocument(value);
			
			Jedis jedis= DatabaseConnection.getInstance().getRedis();
			jedis.hmset(hMap.get("message_id"),hMap);
			//TODO
			
			//send post add url
			 URL url = new URL("");
			 HttpURLConnection connection = null;
			 connection = (HttpURLConnection) url.openConnection();
			    connection.setRequestMethod("POST");
			    connection.setRequestProperty("Content-Type", 
			        "application/x-www-form-urlencoded");
			
//			    connection.setRequestProperty("Content-Length", 
//			            Integer.toString(urlParameters.getBytes().length));
//			        connection.setRequestProperty("Content-Language", "en-US");  
//
//			        connection.setUseCaches(false);
//			        connection.setDoOutput(true);
//
//			        //Send request
//			        DataOutputStream wr = new DataOutputStream (
//			            connection.getOutputStream());
//			        wr.writeBytes(urlParameters);
//			        wr.close();
			
			
			
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
