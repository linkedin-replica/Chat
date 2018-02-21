package com.linkedIn.chat.models;

import java.util.Date;

public class Message {
public String sentFrom;
public String sentTo;
public Date sentDate;
public boolean read;
public Date readDate;
public String msg;
public Message(String sentFrom, String sentTo,Date sentDate, boolean read,Date readDate,String msg) {
	this.sentFrom = sentFrom;
	this.sentTo = sentTo;
	this.sentDate = sentDate;
	this.read = read;
	this.readDate = readDate;
	this.msg = msg;
}

	public String getMsg() {
	return msg;
}

public void setMsg(String msg) {
	this.msg = msg;
}

	public String getSentFrom() {
	return sentFrom;
}

public void setSentFrom(String sentFrom) {
	this.sentFrom = sentFrom;
}

public String getSentTo() {
	return sentTo;
}

public void setSentTo(String sentTo) {
	this.sentTo = sentTo;
}

public Date getSentDate() {
	return sentDate;
}

public void setSentDate(Date sentDate) {
	this.sentDate = sentDate;
}

public boolean isRead() {
	return read;
}

public void setRead(boolean read) {
	this.read = read;
}

public Date getReadDate() {
	return readDate;
}

public void setReadDate(Date readDate) {
	this.readDate = readDate;
}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
