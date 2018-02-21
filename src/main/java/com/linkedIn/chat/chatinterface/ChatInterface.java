package com.linkedIn.chat.chatinterface;

import com.linkedIn.chat.models.Message;

public interface ChatInterface {
public void insertMessage(Message msg);
public void getMessage(String userId);
public void markAsRead(Message msg);
}
