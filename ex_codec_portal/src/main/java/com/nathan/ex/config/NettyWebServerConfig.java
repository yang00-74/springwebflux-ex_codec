package com.nathan.ex.config;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.embedded.NettyWebServerFactoryCustomizer;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import reactor.netty.http.server.HttpServer;

@Configuration
@Slf4j
public class NettyWebServerConfig extends NettyWebServerFactoryCustomizer {

    @Autowired
    private WebFluxServerInitializer webFluxServerInitializer;

    public NettyWebServerConfig(Environment environment, ServerProperties serverProperties) {
        super(environment, serverProperties);
    }

    @Override
    public void customize(NettyReactiveWebServerFactory factory) {
        super.customize(factory);
        factory.addServerCustomizers(new NettyCustomizer());
    }


    class NettyCustomizer implements NettyServerCustomizer {

        @Override
        public HttpServer apply(HttpServer httpServer) {
            return httpServer.tcpConfiguration(
                    tcpServer -> tcpServer.bootstrap(serverBootstrap ->
                            serverBootstrap
                                    .childHandler(webFluxServerInitializer)
                                    .handler(new LoggingHandler(LogLevel.DEBUG))));
        }
    }

}
