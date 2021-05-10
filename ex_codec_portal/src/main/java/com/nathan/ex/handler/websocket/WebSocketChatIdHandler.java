package com.nathan.ex.handler.websocket;

import com.nathan.ex.annotation.WebSocketRequestMapping;
import com.nathan.ex.common.WebSocketSender;
import com.nathan.ex.common.WebSocketSenderManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@WebSocketRequestMapping("/echo")
@Slf4j
public class WebSocketChatIdHandler implements WebSocketHandler {

    /**
     * ws://localhost:8080/echo?id=567
     * websocket 网页 https://www.websocket.org/echo.html
     * */
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        HandshakeInfo handshakeInfo = session.getHandshakeInfo();
        Map<String, String> queryMap = getQueryMap(handshakeInfo.getUri().getQuery());
        String id = queryMap.getOrDefault("id", "defaultId");

        Mono<Void> input = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(msg -> id + ": " + msg)
                .doOnNext(msg -> {
                    log.info("msg:{}", msg);
                    WebSocketSenderManager.pushToAllOthers(id, msg);
                })
                .then();

        Mono<Void> output = session.send(Flux.create(sink ->
                WebSocketSenderManager.registerSender(id, new WebSocketSender(session, sink))));

        return Mono.zip(input, output)
                .then()
                .doFinally(signalType -> {
                    WebSocketSenderManager.unregisterSender(id);
                    log.warn("out close id:{}", id);
                });
    }

    //用于获取url参数
    private Map<String, String> getQueryMap(String queryStr) {
        Map<String, String> queryMap = new HashMap<>();
        if (!StringUtils.isEmpty(queryStr)) {
            String[] queryParam = queryStr.split("&");
            Arrays.stream(queryParam).forEach(s -> {
                String[] kv = s.split("=", 2);
                String value = kv.length == 2 ? kv[1] : "";
                queryMap.put(kv[0], value);
            });
        }
        return queryMap;
    }
}
