package com.linkedin.replica.chat.realtime.listeners;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.linkedin.replica.chat.realtime.RealtimeDataHandler;

public class ChatDisconnectListener implements DisconnectListener{
    private RealtimeDataHandler realtimeDataHandler = RealtimeDataHandler.getInstance();

    @Override
    public void onDisconnect(SocketIOClient socketIOClient) {
        realtimeDataHandler.disconnectUser(socketIOClient.getSessionId().toString());
    }
}
