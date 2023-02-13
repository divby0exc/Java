package com.divby0exc.wswebappwithpostman.handler;

import com.divby0exc.wswebappwithpostman.encoding.MessageDecoder;
import com.divby0exc.wswebappwithpostman.encoding.MessageEncoder;
import com.divby0exc.wswebappwithpostman.model.DTOUser;
import com.divby0exc.wswebappwithpostman.model.Message;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerEndpoint("/sub")
public class ServerWebSocketHandler extends TextWebSocketHandler {
    final Logger logger = LoggerFactory.getLogger(ServerWebSocketHandler.class);
    private List<WebSocketSession> userDetails = new ArrayList<>();
    private Message registeredChannel = null;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        userDetails.add(session);
    }

    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String request = message.getPayload();
        logger.atInfo().log("Server recieved: {}", request);
        if(registeredChannel!=null)
            session.sendMessage(new TextMessage(registeredChannel.getFrom() + "\n" + registeredChannel.getContent()));
        //String response = String.format("response from servr to '%s'", HtmlUtils.htmlEscape(request));
        //logger.atInfo().log("Server sends: {}", response);

        for (WebSocketSession sessions : userDetails) {
            if(sessions.isOpen()) {
                sessions.sendMessage(new TextMessage(request));
            }
        }
    }

    @Scheduled(fixedRate = 10000)
    void sendPeriodicMessages() throws IOException {
        for (WebSocketSession session : userDetails) {
            if(session.isOpen()) {
                String broadcast = "server periodic message " + LocalTime.now();
                logger.atInfo().log("Server sendsa: {}", broadcast);
                session.sendMessage(new TextMessage(broadcast));
            }
        }
    }

    public Message getRegisteredChannel() {
        return registeredChannel;
    }

    public void setRegisteredChannel(Message registeredChannel) {
        this.registeredChannel = registeredChannel;
    }
}
