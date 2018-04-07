package com.linkedin.replica.chat.commands.impl;


import com.linkedin.replica.chat.commands.Command;
import com.linkedin.replica.chat.database.handlers.RegisterHandler;

import java.util.HashMap;

public class RegisterSessionCommand extends Command {
    public RegisterSessionCommand(HashMap<String, Object> args) {
        super(args);
    }

    @Override
    public Object execute() {
        RegisterHandler handler = (RegisterHandler) this.dbHandler;
        validateArgs(new String[] {"senderId", "receiverId"});
        String senderId = args.get("senderId").toString();
        String receiverId = args.get("receiverId").toString();
        String offset = null;
        String limit = null;
        
        if(args.containsKey("offset"))
        	offset = args.get("offset").toString();
        
        if(args.containsKey("limit"))
        	limit = args.get("limit").toString();
        
        return handler.register(senderId, receiverId, offset, limit);
    }
}
