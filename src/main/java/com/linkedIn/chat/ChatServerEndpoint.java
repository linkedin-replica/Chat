package com.linkedIn.chat;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedIn.chat.models.Message;
import com.linkedIn.chat.models.Room;


@ServerEndpoint(value = "/chat")
public class ChatServerEndpoint {
    private Logger log = Logger.getLogger(ChatServerEndpoint.class.getSimpleName());
    private Room room = Room.getRoom();

    @OnOpen
    public void open(final Session session, EndpointConfig config) {}

    @OnMessage
    public void onMessage(final Session session, final String messageJson) {
        ObjectMapper mapper = new ObjectMapper();
        Message chatMessage = null;
        try {
            chatMessage = mapper.readValue(messageJson, Message.class);
        } catch (IOException e) {
            String message = "Badly formatted message";
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, message));
            } catch (IOException ex) { log.severe(ex.getMessage()); }
        } ;

        Map<String, Object> properties = session.getUserProperties();
//        if (chatMessage.getMessageType() == MessageType.LOGIN) {
//            String name = chatMessage.getMsg();
//            properties.put("name", name);
//            room.join(session);
//            room.sendMessage(name + " - Joined the chat room");
//        }
//        else {
            String name = (String)properties.get("name");
            room.sendMessage(name + " - " + chatMessage.getMsg());
       // }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        room.leave(session);
       // room.sendMessage((String)session.getUserProperties().get("name") + " - Left the room");
    }

    @OnError
    public void onError(Session session, Throwable ex) { log.info("Error: " + ex.getMessage()); }
}