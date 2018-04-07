package com.linkedin.replica.chat.realtime;

public class ChatObject {
    private String message;
    private String threadToken;

    public ChatObject() {
    }

    public ChatObject(String message, String threadToken) {
        this.message = message;
        this.threadToken = threadToken;
    }

    public String getThreadToken() {
        return threadToken;
    }
    public void setThreadToken(String threadToken) {
        this.threadToken = threadToken;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
