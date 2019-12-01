package com.nathan.ex.config.codec;

import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Date: 2019-11-15
 * @author: nathan.yang
 */
class ExCodecSupport {
    static final List<MimeType> MIME_TYPES = Collections.unmodifiableList(
            Arrays.asList(
                    new MimeType("application", "json"),
                    new MimeType("application","ex-codec")));

    static final String DELIMITED_KEY = "delimited";

    static final String DELIMITED_VALUE = "true";

    boolean supportsMimeType(@Nullable MimeType mimeType) {
        return (mimeType == null || MIME_TYPES.stream().anyMatch(m -> m.isCompatibleWith(mimeType)));
    }

    List<MimeType> getMimeTypes() {
        return MIME_TYPES;
    }
}
