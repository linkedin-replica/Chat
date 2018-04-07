package com.linkedin.replica.chat.database.handlers;

import com.linkedin.replica.chat.models.ChatSessionInfo;

public interface RealtimeHandler extends DatabaseHandler{
    public ChatSessionInfo getSessionInfo(String senderId, String receiverId);
}
