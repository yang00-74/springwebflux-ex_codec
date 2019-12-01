package com.nathan.ex.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author nathan.yang
 * @date 2019/11/29
 */
@Component
public class CoolHandler {

    public Mono<ServerResponse> sayHi(ServerRequest request) {
        String reqParam = request.queryParams().getFirst("acct");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("Hi , this is SpringWebFlux. Got:" + reqParam));
    }

    public Mono<ServerResponse> sayBye(ServerRequest request) {

        Mono<String> content = request.bodyToMono(String.class);
        return content.flatMap(s -> {
            System.out.println(s);
            return ServerResponse.ok().contentType(new MediaType("application", "ex-codec"))
                    .body(BodyInserters.fromValue(s + "\nBye , this is SpringWebFlux"));
        });
    }
}
