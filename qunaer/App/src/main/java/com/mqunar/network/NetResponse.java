package com.mqunar.network;

public class NetResponse {
    public int code = -1;
    public Exception e;
    public int error = 0;
    public Headers headers;
    public int id;
    public boolean redirect = false;
    public byte[] result;
    public int resultLen;
    public long total = -1;
}
