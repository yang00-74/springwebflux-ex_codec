package com.nathan.ex.handler.websocket;

import com.nathan.ex.common.WebSocketSender;
import com.nathan.ex.common.WebSocketSenderManager;
import com.nathan.ex.service.websocket.impl.WebSocketMessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Service
@Slf4j
public class WebSocketCoolHandler implements WebSocketHandler {

    @Resource
    private WebSocketMessageServiceImpl webSocketMessageServiceImpl;

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        Mono<Void> input = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(payload -> {
                    log.info("WebSocket session:{}, received:{}", session.getId(), payload);
                    webSocketMessageServiceImpl.handleMessage(payload, session.getId());
                })
                .doOnError(e -> log.error("e:{}", e))
                .then();

        Flux<WebSocketMessage> source = Flux.create(sink -> WebSocketSenderManager.registerSender(session.getId(), new WebSocketSender(session, sink)));
        Mono<Void> output = session.send(source);

        return Mono.zip(input, output)
                .then()
                .doFinally(signalType -> {
                    WebSocketSenderManager.unregisterSender(session.getId());
                });
    }
}
