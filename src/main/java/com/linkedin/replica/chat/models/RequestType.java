package com.linkedin.replica.chat.models;
public enum RequestType {
	INSERT("send.msg"), 
	MARKASREAD("mark.read"), 
	HISTORY("get.history");
	
	private String commandName;
	
	private RequestType(String commandName){
		this.commandName = commandName;
	}
	
	public String getCommandName(){
		return commandName;
	}
}