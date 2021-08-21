package com.nathan.ex;

import com.nathan.ex.function.FunctionCaller;
import com.nathan.ex.function.RunFunction;
import com.nathan.ex.function.RunHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootTest
class WebFluxApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("Context loaded!");
    }

    @Test
    void testRunFunction () {
        FunctionCaller.call(RunHandler::run);

        RunFunction<String> runFunction = RunHandler::fly;
        FunctionCaller.call(runFunction);

        WebClient webClient = WebClient.create();
        Mono<String> mono = webClient.post()
                .uri("http://localhost:8080/bye")
                .bodyValue("{\"borrowerId\": \"12346309\"}")
                .retrieve()
                .bodyToMono(String.class);

        mono.subscribe(System.out::println);

        System.out.println("webclient");

        mono.block();

    }

}
