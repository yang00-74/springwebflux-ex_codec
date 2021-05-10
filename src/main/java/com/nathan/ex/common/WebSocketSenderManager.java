package com.nathan.ex.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebSocketSenderManager {

    private static final Map<String, WebSocketSender> WEBSOCKET_SENDERS = new ConcurrentHashMap<>();
    private static final Map<String, String> USER_SESSIONS = new ConcurrentHashMap<>();

    public static void registerUser(String uid, String sessionId) {
        log.info("register user:{}", uid);
        USER_SESSIONS.putIfAbsent(uid, sessionId);
    }

    public static void unRegisterUser(String uid) {
        log.info("unregister user:{}", uid);
        USER_SESSIONS.remove(uid);
    }

    public static void registerSender(String sessionId, WebSocketSender sender) {
        log.info("register session:{}", sessionId);
        WEBSOCKET_SENDERS.putIfAbsent(sessionId, sender);
    }

    public static void unregisterSender(String sessionId) {
        if (StringUtils.isEmpty(sessionId)) {
            return;
        }
        log.info("unregister session:{}", sessionId);
        WEBSOCKET_SENDERS.remove(sessionId);
        if (!USER_SESSIONS.containsValue(sessionId)) {
            return;
        }

        USER_SESSIONS.entrySet().parallelStream()
                .filter(entry -> sessionId.equals(entry.getValue()))
                .findAny()
                .ifPresent(target -> USER_SESSIONS.remove(target.getKey()));
    }

    public static void pushToAllOthers(String sessionId, String message) {

        WEBSOCKET_SENDERS.forEach((key, sender) -> {
            if (!key.equals(sessionId)) {
                sender.sendData(message);
            }
        });
    }

    public static void sendTo(String receiverUid, String message) {
        if (!USER_SESSIONS.containsKey(receiverUid)) {
            log.info("user offline:{}", receiverUid);
            return;
        }
        String sessionId = USER_SESSIONS.get(receiverUid);
        if (!WEBSOCKET_SENDERS.containsKey(sessionId)) {
            log.info("user offline:{}", receiverUid);
            return;
        }
        WEBSOCKET_SENDERS.get(sessionId).sendData(message);
    }
}
