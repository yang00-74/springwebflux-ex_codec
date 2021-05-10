package com.nathan.ex.common;

import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;

public class WebSocketSender {

    private WebSocketSession session;

    private FluxSink<WebSocketMessage> sink;

    public WebSocketSender(WebSocketSession session, FluxSink<WebSocketMessage> sink) {
        this.session = session;
        this.sink = sink;
    }

    public WebSocketSender(WebSocketSession session) {
        this.session = session;
    }

    public void sendData(String data) {
        sink.next(session.textMessage(data));
    }
}
