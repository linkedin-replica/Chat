package com.linkedin.replica.chat.models;

import java.util.ArrayList;
import java.util.List;

public class RegisterSessionInfo {
    private String ip;
    private String port;
    private String sessionToken;
    private List<Message> history;

    public RegisterSessionInfo(String ip, String port, String sessionToken, List<Message> history) {
        this.ip = ip;
        this.port = port;
        this.sessionToken = sessionToken;
        this.history = history;
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

	public List<Message> getHistory() {
		return history;
	}
}
