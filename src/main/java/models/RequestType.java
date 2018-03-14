package models;
public enum RequestType {
	USERS("search.user"), 
	COMPANIES("search.company"), 
	JOBS("search.job"), 
	POSTS("search.post");
	
	private String commandName;
	
	private RequestType(String commandName){
		this.commandName = commandName;
	}
	
	public String getCommandName(){
		return commandName;
	}
}