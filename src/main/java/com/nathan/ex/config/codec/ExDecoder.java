package com.nathan.ex.config.codec;

import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Decoder;
import org.springframework.core.codec.DecodingException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @Date: 2019-11-15 19:18:15
 * @Author: nathan.yang
 */
public class ExDecoder extends ExCodecSupport implements Decoder<Object> {

    /**
     * The default max size for aggregating messages.
     */
    protected static final int DEFAULT_MESSAGE_MAX_SIZE = 64 * 1024;

    private int maxMessageSize = DEFAULT_MESSAGE_MAX_SIZE;

    /**
     * Construct a new {@code ProtobufDecoder}.
     */
    public ExDecoder() {
    }

    public void setMaxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }

    @Override
    public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
        boolean canDecode = Object.class.isAssignableFrom(elementType.toClass()) && supportsMimeType(mimeType);
        return canDecode;
    }

    @Override
    public Flux<Object> decode(Publisher<DataBuffer> inputStream, ResolvableType elementType,
                                @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
        return Flux.from(inputStream)
                .flatMapIterable(new ExDecoder.MessageDecoderFunction(elementType, this.maxMessageSize));
    }

    @Override
    public Mono<Object> decodeToMono(Publisher<DataBuffer> inputStream, ResolvableType elementType,
                                     @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {

        return DataBufferUtils.join(inputStream).map(dataBuffer -> {
                    try {
                        // @{ read input message Mono
                        ByteBuffer buffer = dataBuffer.asByteBuffer();
                        String originInputMessage = StandardCharsets.UTF_8.decode(buffer).toString();
                        String destInputMessage = originInputMessage + "-- [by ExDecoder, Mono]";
                        // @}
                        return destInputMessage;
                    } catch (Exception ex) {
                        throw new DecodingException("Could not read message: " + ex.getMessage(), ex);
                    } finally {
                        DataBufferUtils.release(dataBuffer);
                    }
                }
        );
    }

    @Override
    public List<MimeType> getDecodableMimeTypes() {
        return getMimeTypes();
    }


    private class MessageDecoderFunction implements Function<DataBuffer, Iterable<? extends Object>> {

        private final ResolvableType elementType;

        private final int maxMessageSize;

        @Nullable
        private DataBuffer output;

        private int messageBytesToRead;

        private int offset;


        public MessageDecoderFunction(ResolvableType elementType, int maxMessageSize) {
            this.elementType = elementType;
            this.maxMessageSize = maxMessageSize;
        }

        @Override
        public Iterable<? extends Object> apply(DataBuffer input) {
            try {
                List<Object> messages = new ArrayList<>();
                int remainingBytesToRead;
                int chunkBytesToRead;
                do {
                    if (this.output == null) {
                        if (!readMessageSize(input)) {
                            return messages;
                        }
                        if (this.messageBytesToRead > this.maxMessageSize) {
                            throw new DecodingException(
                                    "The number of bytes to read from the incoming stream " +
                                            "(" + this.messageBytesToRead + ") exceeds " +
                                            "the configured limit (" + this.maxMessageSize + ")");
                        }
                        this.output = input.factory().allocateBuffer(this.messageBytesToRead);
                    }

                    chunkBytesToRead = Math.min(this.messageBytesToRead, input.readableByteCount());
                    remainingBytesToRead = input.readableByteCount() - chunkBytesToRead;

                    byte[] bytesToWrite = new byte[chunkBytesToRead];
                    input.read(bytesToWrite, 0, chunkBytesToRead);
                    this.output.write(bytesToWrite);
                    this.messageBytesToRead -= chunkBytesToRead;

                    if (this.messageBytesToRead == 0) {
                        // @{ read input message Flux
                        ByteBuffer buffer = this.output.asByteBuffer();
                        String originInputMessage = StandardCharsets.UTF_8.decode(buffer).toString();
                        String destInputMessage = originInputMessage + "-- [by ExDecoder, Flux]";
                        messages.add(destInputMessage);
                        // @}
                        DataBufferUtils.release(this.output);
                        this.output = null;
                    }
                } while (remainingBytesToRead > 0);
                return messages;
            } catch (DecodingException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new DecodingException("Could not read Protobuf message: " + ex.getMessage(), ex);
            } finally {
                DataBufferUtils.release(input);
            }
        }

        /**
         * Parse message size as a varint from the input stream, updating {@code messageBytesToRead} and
         * {@code offset} fields if needed to allow processing of upcoming chunks.
         * Inspired from {@link CodedInputStream#readRawVarint32(int, java.io.InputStream)}
         *
         * @return {code true} when the message size is parsed successfully, {code false} when the message size is
         * truncated
         * @see <a href ="https://developers.google.com/protocol-buffers/docs/encoding#varints">Base 128 Varints</a>
         */
        private boolean readMessageSize(DataBuffer input) {
            if (this.offset == 0) {
                if (input.readableByteCount() == 0) {
                    return false;
                }
                int firstByte = input.read();
                if ((firstByte & 0x80) == 0) {
                    this.messageBytesToRead = firstByte;
                    return true;
                }
                this.messageBytesToRead = firstByte & 0x7f;
                this.offset = 7;
            }

            if (this.offset < 32) {
                for (; this.offset < 32; this.offset += 7) {
                    if (input.readableByteCount() == 0) {
                        return false;
                    }
                    final int b = input.read();
                    this.messageBytesToRead |= (b & 0x7f) << offset;
                    if ((b & 0x80) == 0) {
                        this.offset = 0;
                        return true;
                    }
                }
            }
            // Keep reading up to 64 bits.
            for (; this.offset < 64; this.offset += 7) {
                if (input.readableByteCount() == 0) {
                    return false;
                }
                final int b = input.read();
                if ((b & 0x80) == 0) {
                    this.offset = 0;
                    return true;
                }
            }
            this.offset = 0;
            throw new DecodingException("Cannot parse message size: malformed varint");
        }
    }

}
