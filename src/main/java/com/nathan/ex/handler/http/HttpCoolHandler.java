package com.nathan.ex.handler.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author nathan.yang
 * @date 2019/11/29
 */
@Service
@Slf4j
public class HttpCoolHandler {

    public Mono<ServerResponse> sayHi(ServerRequest request) {
        String reqParam = request.queryParams().getFirst("acct");
        log.info("HTTP receive:{}", reqParam);

        MediaType mediaType = request.headers()
                .contentType()
                .orElse(MediaType.APPLICATION_JSON);

        return ServerResponse.ok().contentType(mediaType)
                .body(BodyInserters.fromValue("Hi , this is SpringWebFlux. Got:" + reqParam));
    }

}
