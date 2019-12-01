package com.nathan.ex;

import com.nathan.ex.function.FunctionCaller;
import com.nathan.ex.function.RunFunction;
import com.nathan.ex.function.RunHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
    }

}
