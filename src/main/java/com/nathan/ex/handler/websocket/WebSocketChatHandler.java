package com.nathan.ex.handler.websocket;

import com.nathan.ex.annotation.WebSocketRequestMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@WebSocketRequestMapping("/websocket/chat")
public class WebSocketChatHandler implements WebSocketHandler {

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        Flux<WebSocketMessage> messageFlux = session.receive()
                .map(message -> {
                    String payload = message.getPayloadAsText();
                    log.info("WebSocket session:{}, received:{}", session.getId(), payload);
                    return payload;
                })
                .map(session::textMessage);
        return session.send(messageFlux).doFinally(signalType -> {log.warn("out close");});
    }
}
