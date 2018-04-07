package com.linkedin.replica.chat.commands.impl;


import com.linkedin.replica.chat.commands.Command;
import com.linkedin.replica.chat.database.handlers.RealtimeHandler;

import java.util.HashMap;

public class RegisterSessionCommand extends Command {
    public RegisterSessionCommand(HashMap<String, Object> args) {
        super(args);
    }

    @Override
    public Object execute() {
        RealtimeHandler handler = (RealtimeHandler) this.dbHandler;
        validateArgs(new String[] {"senderId", "receiverId"});
        String senderId = args.get("senderId").toString();
        String receiverId = args.get("receiverId").toString();

        return handler.getSessionInfo(senderId, receiverId);
    }
}
