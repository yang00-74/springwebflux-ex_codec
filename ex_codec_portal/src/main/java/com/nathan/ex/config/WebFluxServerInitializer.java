package com.nathan.ex.config;

import com.nathan.ex.handler.websocket.HeartBeatServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class WebFluxServerInitializer extends ChannelInitializer<SocketChannel> {

    @Value("${websocket.ssl:false}")
    private boolean SSL;

    @Autowired
    private HeartBeatServerHandler heartBeatServerHandler;

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        // Configure SSL.
        final SslContext sslCtx;

        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        //每60秒检测下
        pipeline.addLast(new IdleStateHandler(60,0,0, TimeUnit.SECONDS));
        pipeline.addLast(heartBeatServerHandler);
    }
}
