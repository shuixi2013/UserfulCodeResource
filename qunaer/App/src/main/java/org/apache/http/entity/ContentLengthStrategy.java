package org.apache.http.entity;

import org.apache.http.HttpMessage;

@Deprecated
public interface ContentLengthStrategy {
    public static final int CHUNKED = -2;
    public static final int IDENTITY = -1;

    long determineLength(HttpMessage httpMessage);
}
