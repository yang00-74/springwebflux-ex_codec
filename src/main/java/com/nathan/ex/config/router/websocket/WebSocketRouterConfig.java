package com.nathan.ex.config.router.websocket;

import com.nathan.ex.annotation.WebSocketRequestMapping;
import com.nathan.ex.annotation.handler.WebSocketMappingHandlerMapping;
import com.nathan.ex.handler.websocket.WebSocketCoolHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class WebSocketRouterConfig {

    /**
     * 手动配置 websocket handler
     * */
    @Bean
    public HandlerMapping webSocketMapping(WebSocketCoolHandler coolHandler) {
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();

        Map<String, WebSocketHandler> handlerMap = new LinkedHashMap<>();
        handlerMap.put("/websocket", coolHandler);

        simpleUrlHandlerMapping.setUrlMap(handlerMap);
        simpleUrlHandlerMapping.setOrder(-1);
        return simpleUrlHandlerMapping;
    }

    /**
     * 使用注解 {@link WebSocketRequestMapping} 自动配置
     * */
    @Bean
    public HandlerMapping webSocketRequestMapping() {
        return new WebSocketMappingHandlerMapping();
    }

    /**
     * WebSocket 必须使用的适配器
     * */
    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
