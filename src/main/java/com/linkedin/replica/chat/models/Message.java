package com.linkedin.replica.chat.models;

import com.arangodb.entity.DocumentField;
import java.util.Date;

public class Message {

	@DocumentField(DocumentField.Type.KEY)
	private String id;

	private String sender;
	private String receiver;
	private long timestamp;
	private String msg;

	public Message() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
