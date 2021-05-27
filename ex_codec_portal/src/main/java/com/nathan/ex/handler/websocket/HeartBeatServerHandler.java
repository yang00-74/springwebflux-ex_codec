package com.nathan.ex.handler.websocket;

import com.nathan.ex.util.ChannelUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 心跳检测
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {

    @Value("${heartBeat.lossConnectThreshold:4}")
    private Integer lossConnectThreshold;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                Long lossConnectCount = ChannelUtil.recordLossConnect(ctx.channel().id().asShortText());
                log.info("heart beat channelId:{}, lossConnectCount:{}", ctx.channel().id(), lossConnectCount);
                if (lossConnectCount > lossConnectThreshold) {
                    ctx.channel().close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ChannelUtil.resetLossConnect(ctx.channel().id().asShortText());
        ctx.fireChannelRead(msg);
    }


}
