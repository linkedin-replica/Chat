package com.linkedin.replica.chat.commands;

import java.util.HashMap;

import com.linkedin.replica.chat.database.handlers.DatabaseHandler;

public abstract class Command {

	public HashMap<String, String> args;
	public DatabaseHandler dbHandler;

	public Command(HashMap<String, String> args) {
		this.args = args;
	}

	public abstract String execute() throws Exception;
	
	public void setDbHandler(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    protected void validateArgs(String[] requiredArgs) {
        for(String arg: requiredArgs)
            if(!args.containsKey(arg)) {
                String exceptionMsg = String.format("Cannot execute command. %s argument is missing", arg);
                throw new IllegalArgumentException(exceptionMsg);
            }
    }
}
