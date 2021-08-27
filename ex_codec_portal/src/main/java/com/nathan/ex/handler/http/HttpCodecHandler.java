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
public class HttpCodecHandler {

    /**
     * 触发 ExDecoder/ExEncoder 解码编码对应类型
     * curl -X POST --data-raw '{"borrowerId": "12346309"}' 'http://127.0.0.1:8080/bye' --header 'Content-Type: application/ex-codec'
     * */
    public Mono<ServerResponse> sayBye(ServerRequest request) {
        Mono<String> content = request.bodyToMono(String.class);

        MediaType mediaType = request.headers()
                .contentType()
                .orElse(MediaType.APPLICATION_JSON);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return content.flatMap(s -> {
            log.info("HTTP receive:{}", s);
            return ServerResponse.ok()
                    .contentType(mediaType)
                    .body(BodyInserters.fromValue(s + "\nBye, this is SpringWebFlux"));
        });
    }
}
