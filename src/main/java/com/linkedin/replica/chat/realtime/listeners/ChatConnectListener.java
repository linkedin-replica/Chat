package com.linkedin.replica.chat.realtime.listeners;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.linkedin.replica.chat.realtime.RealtimeDataHandler;
import com.linkedin.replica.chat.utils.JwtUtilities;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class ChatConnectListener implements ConnectListener{
    private RealtimeDataHandler realtimeDataHandler = RealtimeDataHandler.getInstance();

    @Override
    public void onConnect(SocketIOClient socketIOClient) {
        String token = socketIOClient.getHandshakeData().getSingleUrlParam("threadToken");
        Jws<Claims> claims = JwtUtilities.getClaims(token);

        if(claims == null)
            return;

        String senderId = claims.getBody().get("senderId").toString();
        System.out.println("User " + senderId + " connected successfully");

        realtimeDataHandler.connectUser(senderId, socketIOClient.getSessionId().toString());
    }
}
