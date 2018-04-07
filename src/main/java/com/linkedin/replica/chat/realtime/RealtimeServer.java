package com.linkedin.replica.chat.realtime;


import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.linkedin.replica.chat.realtime.listeners.ChatConnectListener;
import com.linkedin.replica.chat.realtime.listeners.ChatDataListener;
import com.linkedin.replica.chat.realtime.listeners.ChatDisconnectListener;
import io.netty.channel.ChannelFuture;

import java.util.concurrent.ConcurrentHashMap;

public class RealtimeServer {
    private SocketIOServer server;
    private ConcurrentHashMap<String, String> idToSessionMap, sessionToIdMap;

    public RealtimeServer(String ip, int port) {
        // set connections config
        Configuration config = new Configuration();
        config.setHostname(ip);
        config.setPort(port);

        idToSessionMap  = new ConcurrentHashMap<>();
        sessionToIdMap = new ConcurrentHashMap<>();

        server = new SocketIOServer(config);
        addListeners();

        try {
            start();
        }
        finally {
            stop();
        }
    }

    private void addListeners() {
        server.addEventListener("chatevent", ChatObject.class, new ChatDataListener());
        server.addConnectListener(new ChatConnectListener());
        server.addDisconnectListener(new ChatDisconnectListener());
    }

    private void start() {
        ((ChannelFuture) server.startAsync().syncUninterruptibly()).channel().closeFuture().syncUninterruptibly();
    }

    private void stop() {
        server.stop();
    }
}
