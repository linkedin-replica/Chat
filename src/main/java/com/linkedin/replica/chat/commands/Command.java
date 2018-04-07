package com.linkedin.replica.chat.commands;

import java.util.HashMap;

import com.linkedin.replica.chat.database.handlers.DatabaseHandler;
import com.linkedin.replica.chat.exceptions.BadRequestException;

public abstract class Command {

	public HashMap<String, Object> args;
	public DatabaseHandler dbHandler;

	public Command(HashMap<String, Object> args) {
		this.args = args;
	}

	public abstract String execute();
	
	public void setDbHandler(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    protected void validateArgs(String[] requiredArgs) {
        for(String arg: requiredArgs)
            if(!args.containsKey(arg)) {
                String exceptionMsg = String.format("Cannot execute command. %s argument is missing", arg);
                throw new BadRequestException(exceptionMsg);
            }
    }
}
