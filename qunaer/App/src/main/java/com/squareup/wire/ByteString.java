package com.squareup.wire;

import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public final class ByteString {
    public static final ByteString EMPTY = of(new byte[0]);
    private final byte[] data;
    private transient int hashCode;

    public static ByteString of(byte... bArr) {
        return new ByteString((byte[]) bArr.clone());
    }

    public static ByteString of(byte[] bArr, int i, int i2) {
        Object obj = new byte[i2];
        System.arraycopy(bArr, i, obj, 0, i2);
        return new ByteString(obj);
    }

    public static ByteString of(String str) {
        return new ByteString(Stringer.decode(str));
    }

    public static ByteString read(InputStream inputStream, int i) {
        byte[] bArr = new byte[i];
        int i2 = 0;
        while (i2 < i) {
            int read = inputStream.read(bArr, i2, i - i2);
            if (read == -1) {
                throw new EOFException("Expected " + i + "; received " + i2);
            }
            i2 += read;
        }
        return new ByteString(bArr);
    }

    private ByteString(byte[] bArr) {
        this.data = bArr;
    }

    public byte byteAt(int i) {
        return this.data[i];
    }

    public int size() {
        return this.data.length;
    }

    public byte[] toByteArray() {
        return (byte[]) this.data.clone();
    }

    public void write(OutputStream outputStream) {
        outputStream.write(this.data);
    }

    public void write(OutputStream outputStream, int i, int i2) {
        outputStream.write(this.data, i, i2);
    }

    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof ByteString) && Arrays.equals(((ByteString) obj).data, this.data));
    }

    public int hashCode() {
        int i = this.hashCode;
        if (i != 0) {
            return i;
        }
        i = Arrays.hashCode(this.data);
        this.hashCode = i;
        return i;
    }

    public String toString() {
        return Stringer.encode(this.data);
    }
}
