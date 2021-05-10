package com.nathan.ex.config.codec;

import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.EncodingException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageEncoder;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.CoderMalfunctionError;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: nathan.yang
 * @date: 2019-11-15
 */
public class ExEncoder extends ExCodecSupport implements HttpMessageEncoder<Object> {

    private static final List<MediaType> streamingMediaTypes = MIME_TYPES
            .stream()
            .map(mimeType -> new MediaType(mimeType.getType(), mimeType.getSubtype(),
                    Collections.singletonMap(DELIMITED_KEY, DELIMITED_VALUE)))
            .collect(Collectors.toList());

    public ExEncoder() {
    }

    @Override
    public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
        return Object.class.isAssignableFrom(elementType.toClass()) && supportsMimeType(mimeType);
    }

    /**
     * Used in spring-webflux-5.2.0
     */
    @Override
    public DataBuffer encodeValue(Object value, DataBufferFactory bufferFactory, ResolvableType valueType, MimeType mimeType, Map<String, Object> hints) {

        String originOutputMessage = value.toString();
        String destOutputMessage = originOutputMessage + "-- [by ExEncoder 5.2.0]";
        DataBuffer dataBuffer = bufferFactory.allocateBuffer();
        boolean release = true;
        try {
            dataBuffer.write(destOutputMessage.getBytes(StandardCharsets.UTF_8));
            release = false;
        } catch (CoderMalfunctionError var14) {
            throw new EncodingException("String encoding error: " + var14.getMessage(), var14);
        } finally {
            if (release) {
                DataBufferUtils.release(dataBuffer);
            }
        }
        return dataBuffer;
    }

    /**
     * Used in spring-webflux-5.1.3
     */
    @Override
    public Flux<DataBuffer> encode(Publisher<? extends Object> inputStream, DataBufferFactory bufferFactory,
                                   ResolvableType elementType, @Nullable MimeType mimeType,
                                   @Nullable Map<String, Object> hints) {
        return Flux.from(inputStream)
                .map(message -> encodeMessage(message, bufferFactory, !(inputStream instanceof Mono), elementType));
    }

    private DataBuffer encodeMessage(Object message, DataBufferFactory bufferFactory, boolean streaming, ResolvableType elementType) {
        try {

            DataBuffer buffer = bufferFactory.allocateBuffer();
            String originOutputMessage = message.toString();
            String destOutputMessage = originOutputMessage + "-- [by ExEncoder 5.1.3]";

            return buffer.write(destOutputMessage.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new EncodingException("Could not write message: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<MimeType> getEncodableMimeTypes() {
        return getMimeTypes();
    }

    @Override
    public List<MediaType> getStreamingMediaTypes() {
        return streamingMediaTypes;
    }
}
