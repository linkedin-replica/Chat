package com.linkedin.replica.chat.database.handlers.impl;

import com.linkedin.replica.chat.config.Configuration;
import com.linkedin.replica.chat.database.handlers.RealtimeHandler;
import com.linkedin.replica.chat.models.ChatSessionInfo;
import com.linkedin.replica.chat.utils.JwtUtilities;

public class ConcurrentDataRealtimeHandler implements RealtimeHandler{
    private Configuration config = Configuration.getInstance();

    @Override
    public ChatSessionInfo getSessionInfo(String senderId, String receiverId) {
        String ip = config.getAppConfigProp("chat.ip");
        String port = config.getAppConfigProp("chat.port");

        String token = JwtUtilities.generateToken(senderId, receiverId);
        return new ChatSessionInfo(ip, port, token);
    }
}
