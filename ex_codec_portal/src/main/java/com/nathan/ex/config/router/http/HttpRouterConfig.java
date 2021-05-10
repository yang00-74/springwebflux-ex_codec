package com.nathan.ex.config.router.http;

import com.nathan.ex.handler.http.HttpCodecHandler;
import com.nathan.ex.handler.http.HttpCoolHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author nathan.yang
 * @date 2019/11/29
 */
@Configuration
public class HttpRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routeCoolHandler(HttpCoolHandler coolHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/hi")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), coolHandler::sayHi);
    }

    /**
     * 手动配置的 codec 用例路由
     * */
    @Bean
    public RouterFunction<ServerResponse> routeCodecHandler(HttpCodecHandler codecHandler) {
        return RouterFunctions
                .route(RequestPredicates.POST("/bye")
                        .and(RequestPredicates.accept(new MediaType("application", "ex-codec"))), codecHandler::sayBye);
    }

}
