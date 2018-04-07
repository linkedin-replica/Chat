package com.linkedin.replica.chat.models;

public class ChatSessionInfo {
    private String ip;
    private String port;
    private String sessionToken;

    public ChatSessionInfo(String ip, String port, String sessionToken) {
        this.ip = ip;
        this.port = port;
        this.sessionToken = sessionToken;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
