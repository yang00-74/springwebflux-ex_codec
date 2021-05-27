package com.nathan.ex.util;

import com.google.common.util.concurrent.AtomicLongMap;
import org.springframework.stereotype.Component;

@Component
public class ChannelUtil {

    /**
     * 通道ID-心跳超时次数
     */
    private final static AtomicLongMap<String> LOSS_CONNECT_COUNTER = AtomicLongMap.create();

    public static long recordLossConnect(String channelId) {
        return LOSS_CONNECT_COUNTER.incrementAndGet(channelId);
    }

    public static long resetLossConnect(String channelId) {
        return LOSS_CONNECT_COUNTER.put(channelId, 0);
    }
}
