package com.nathan.ex.config.codec;

import com.nathan.ex.config.codec.constant.MimeTypeConstant;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;

import java.util.Collections;
import java.util.List;

/**
 * @Date: 2019-11-15
 * @author: nathan.yang
 */
class ExCodecSupport {
    static final List<MimeType> MIME_TYPES = Collections.unmodifiableList(
            Collections.singletonList(MimeTypeConstant.EX_CODEC));

    static final String DELIMITED_KEY = "delimited";

    static final String DELIMITED_VALUE = "true";

    boolean supportsMimeType(@Nullable MimeType mimeType) {
        return MIME_TYPES.stream().anyMatch(m -> m.isCompatibleWith(mimeType));
    }

    List<MimeType> getMimeTypes() {
        return MIME_TYPES;
    }
}
