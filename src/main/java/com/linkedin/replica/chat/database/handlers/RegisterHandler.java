package com.linkedin.replica.chat.database.handlers;

import com.linkedin.replica.chat.models.RegisterSessionInfo;

public interface RegisterHandler {
	public RegisterSessionInfo register(String senderId, String receiverId, String offset, String limit);
}
