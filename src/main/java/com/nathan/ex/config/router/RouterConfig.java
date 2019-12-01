package com.nathan.ex.config.router;

import com.nathan.ex.handler.CoolHandler;
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
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routeCoolHandler(CoolHandler coolHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/hi")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), coolHandler::sayHi)
                .andRoute(RequestPredicates.POST("/bye")
                        .and(RequestPredicates.accept(new MediaType("application","json"))),
                        coolHandler::sayBye);
    }

}
