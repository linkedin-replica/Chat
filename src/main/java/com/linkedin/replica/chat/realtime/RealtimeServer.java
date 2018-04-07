package com.linkedin.replica.chat.realtime;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.linkedin.replica.chat.realtime.listeners.ChatConnectListener;
import com.linkedin.replica.chat.realtime.listeners.ChatDataListener;
import com.linkedin.replica.chat.realtime.listeners.ChatDisconnectListener;
import io.netty.channel.ChannelFuture;


public class RealtimeServer {
    private SocketIOServer server;

    public RealtimeServer(String ip, int port) throws InterruptedException {
        // set connections config
        final Configuration config = new Configuration();
        config.setHostname(ip);
        config.setPort(port);

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
        server.addEventListener("chatevent", ChatObject.class, new ChatDataListener(server));
        server.addConnectListener(new ChatConnectListener());
        server.addDisconnectListener(new ChatDisconnectListener());
    }

    private void start() throws InterruptedException {
        ((ChannelFuture) server.startAsync().sync()).channel().closeFuture().sync();
    }

    private void stop() {
        server.stop();
    }
}
