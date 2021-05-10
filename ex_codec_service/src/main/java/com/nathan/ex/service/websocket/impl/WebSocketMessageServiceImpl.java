package com.nathan.ex.service.websocket.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nathan.ex.common.WebSocketSenderManager;
import com.nathan.ex.service.websocket.WebSocketMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WebSocketMessageServiceImpl implements WebSocketMessageService {

    @Override
    public void handleMessage(String message, String sessionId) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        try {

            rootNode = mapper.readTree(message);
            JsonNode uidNode = rootNode.path("uid");
            String uid = uidNode.asText();
            JsonNode eventNode = rootNode.path("event");

            switch (eventNode.asText()) {
                case "register":
                    WebSocketSenderManager.registerUser(uid, sessionId);
                    break;
                case "message":
                    JsonNode dataNode = rootNode.path("data");
                    JsonNode receiverNode = dataNode.path("to");
                    String receiverUid = receiverNode.asText();
                    log.info("send to:{}", receiverUid);
                    if (!receiverUid.startsWith("user")) {
                        WebSocketSenderManager.pushToAllOthers(sessionId, message);
                    } else {
                        WebSocketSenderManager.sendTo(receiverUid, message);
                    }
                    break;
                default:
                    break;
            }

        } catch (JsonProcessingException e) {
           log.error("error:{}", e);
        }

    }
}
