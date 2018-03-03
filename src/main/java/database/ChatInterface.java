package com.linkedIn.chat.chatinterface;

import java.util.List;

import com.linkedIn.chat.models.Message;

public interface ChatInterface {
public void insertMessage(Message msg);
public List<Message> getChatHistory(String userId1,String userId2) ;
public List<Message> getChatHistory(String userId1,String userId2,int limit, int offset) ;
public void markAsRead(String messageId,String readDate);
}
