package com.linkedin.replica.chat.models;

import com.arangodb.entity.DocumentField;
import java.util.Date;

public class Message {

	@DocumentField(DocumentField.Type.KEY)
	private String id;

	private String senderId;
	private String receiverId;
	private long timestamp;
	private String message;

	public Message() {
	}

	public Message(String senderId, String receiverId, long timeStamp, String message) {
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.timestamp = timeStamp;
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String msg) {
		this.message = msg;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
