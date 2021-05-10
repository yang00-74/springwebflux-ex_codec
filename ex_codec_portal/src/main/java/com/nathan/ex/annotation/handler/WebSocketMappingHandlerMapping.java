package com.nathan.ex.annotation.handler;


import com.nathan.ex.annotation.WebSocketRequestMapping;
import org.springframework.beans.BeansException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class WebSocketMappingHandlerMapping extends SimpleUrlHandlerMapping {

    private Map<String, WebSocketHandler> socketHandlerMap = new LinkedHashMap<>();
    /**
     * Register WebSocket handlers annotated by @WebSocketRequestMapping
     * @throws BeansException
     */
    @Override
    public void initApplicationContext() throws BeansException {
        Map<String, Object> beansWithAnnotation =
                obtainApplicationContext().getBeansWithAnnotation(WebSocketRequestMapping.class);
        beansWithAnnotation.values()
                .forEach(bean -> {
                    if (!(bean instanceof WebSocketHandler)) {
                        throw new RuntimeException(
                                String.format("Controller [%s] doesn't implement WebSocketHandler interface.",
                                        bean.getClass().getName()));
                    }
                    WebSocketRequestMapping annotation =
                            AnnotationUtils.getAnnotation(bean.getClass(), WebSocketRequestMapping.class);
                    socketHandlerMap.put(Objects.requireNonNull(annotation).value(), (WebSocketHandler) bean);
                });
        super.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.setUrlMap(socketHandlerMap);
        super.initApplicationContext();
    }
}