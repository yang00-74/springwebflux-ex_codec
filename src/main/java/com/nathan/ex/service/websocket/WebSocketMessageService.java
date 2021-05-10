package com.nathan.ex.service.websocket;

public interface WebSocketMessageService {

    void handleMessage(String message, String sessionId);
}
