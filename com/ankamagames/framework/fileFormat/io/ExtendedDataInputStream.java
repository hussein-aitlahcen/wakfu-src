package com.ankamagames.framework.fileFormat.io;

import org.apache.log4j.*;
import java.nio.*;
import java.io.*;
import com.ankamagames.framework.kernel.utils.*;

public class ExtendedDataInputStream
{
    public static final ByteOrder DEFAULT_ORDERING;
    private static final Logger m_logger;
    private final ByteBuffer m_byteBuffer;
    private int m_lastBooleanBitFieldPosition;
    private byte m_lastBooleanBitFieldIndex;
    private byte m_lastBooleanBitFieldValue;
    private static final ByteBuffer EMPTY_BYTE_BUFFER;
    private byte[] stringBuffer;
    
    protected ExtendedDataInputStream(final ByteBuffer byteBuffer) {
        super();
        this.m_lastBooleanBitFieldPosition = -1;
        this.m_lastBooleanBitFieldIndex = -1;
        this.m_lastBooleanBitFieldValue = 0;
        this.stringBuffer = new byte[128];
        if (byteBuffer == null) {
            throw new IllegalArgumentException("ByteBuffer can't be null");
        }
        (this.m_byteBuffer = byteBuffer).order(ExtendedDataInputStream.DEFAULT_ORDERING);
    }
    
    public static ExtendedDataInputStream wrapFullAndClose(final InputStream stream) throws IOException {
        final ExtendedDataInputStream result = new ExtendedDataInputStream(readFullStream(stream));
        stream.close();
        return result;
    }
    
    public static ExtendedDataInputStream wrap(final ByteBuffer byteBuffer) {
        return new ExtendedDataInputStream(byteBuffer);
    }
    
    public static ExtendedDataInputStream wrap(final ByteBuffer byteBuffer, final ByteOrder ordering) {
        byteBuffer.order(ordering);
        return new ExtendedDataInputStream(byteBuffer);
    }
    
    public static ExtendedDataInputStream wrap(final byte[] data) {
        return new ExtendedDataInputStream(ByteBuffer.wrap(data));
    }
    
    public static ExtendedDataInputStream wrap(final byte[] data, final ByteOrder ordering) {
        return new ExtendedDataInputStream(ByteBuffer.wrap(data).order(ordering));
    }
    
    protected static ByteBuffer readFullStream(final InputStream stream) throws IOException {
        byte[] dataBuffer = null;
        while (stream.available() != 0) {
            final int bufferSize = stream.available();
            final byte[] data = new byte[bufferSize];
            int dataRead;
            for (int bytesInBuffer = 0; bytesInBuffer != bufferSize; bytesInBuffer += dataRead) {
                dataRead = stream.read(data, bytesInBuffer, bufferSize - bytesInBuffer);
                if (dataRead == -1) {
                    throw new EOFException("Less data than assumed in the stream. " + bufferSize + " expected, " + bytesInBuffer + " read");
                }
            }
            if (dataBuffer == null) {
                dataBuffer = data;
            }
            else {
                final byte[] tmp = new byte[dataBuffer.length + data.length];
                System.arraycopy(dataBuffer, 0, tmp, 0, dataBuffer.length);
                System.arraycopy(data, 0, tmp, dataBuffer.length, data.length);
                dataBuffer = tmp;
            }
        }
        if (dataBuffer != null) {
            return ByteBuffer.wrap(dataBuffer);
        }
        return ByteBuffer.allocate(0);
    }
    
    public final void order(final ByteOrder order) {
        this.m_byteBuffer.order(order);
    }
    
    public final ByteOrder order() {
        return this.m_byteBuffer.order();
    }
    
    public final int skip(final int n) {
        if (n <= 0) {
            return 0;
        }
        final int remaining = this.m_byteBuffer.remaining();
        final int bytesToSkip = Math.min(remaining, n);
        this.m_byteBuffer.position(this.m_byteBuffer.position() + bytesToSkip);
        return bytesToSkip;
    }
    
    public final int available() {
        return this.m_byteBuffer.remaining();
    }
    
    public void close() {
    }
    
    public final int readBytes(final byte[] b, final int offset, final int size) {
        final int bytesToRead = Math.min(this.available(), Math.min(b.length - offset, size));
        this.m_byteBuffer.get(b, offset, bytesToRead);
        return bytesToRead;
    }
    
    public final int readBytes(final byte[] b) {
        final int bytesToRead = Math.min(this.available(), b.length);
        this.m_byteBuffer.get(b, 0, bytesToRead);
        return bytesToRead;
    }
    
    public final byte[] readBytes(final int length) {
        final byte[] result = new byte[length];
        this.m_byteBuffer.get(result);
        return result;
    }
    
    public final short[] readShorts(final int length) {
        final short[] result = new short[length];
        for (int i = 0; i < length; ++i) {
            result[i] = this.m_byteBuffer.getShort();
        }
        return result;
    }
    
    public final int[] readInts(final int length) {
        final int[] result = new int[length];
        for (int i = 0; i < length; ++i) {
            result[i] = this.m_byteBuffer.getInt();
        }
        return result;
    }
    
    public final float[] readFloats(final int length) {
        final float[] result = new float[length];
        for (int i = 0; i < length; ++i) {
            result[i] = this.m_byteBuffer.getFloat();
        }
        return result;
    }
    
    public final float readFloat() {
        return this.m_byteBuffer.getFloat();
    }
    
    public final short readShort() {
        return this.m_byteBuffer.getShort();
    }
    
    public final int readUnsignedShort() {
        return this.m_byteBuffer.getShort() & 0xFFFF;
    }
    
    public final int readInt() {
        return this.m_byteBuffer.getInt();
    }
    
    public final long readUnsignedInt() {
        return this.m_byteBuffer.getInt() & 0xFFFFFFFFL;
    }
    
    public final long readLong() {
        return this.m_byteBuffer.getLong();
    }
    
    public final byte readByte() {
        return this.m_byteBuffer.get();
    }
    
    public final short readUnsignedByte() {
        return (short)(this.m_byteBuffer.get() & 0xFF);
    }
    
    public final boolean readBooleanBit() {
        final int currentPosition = this.m_byteBuffer.position();
        if (currentPosition == this.m_lastBooleanBitFieldPosition && this.m_lastBooleanBitFieldIndex <= 6) {
            ++this.m_lastBooleanBitFieldIndex;
            return (this.m_lastBooleanBitFieldValue & 1 << 7 - this.m_lastBooleanBitFieldIndex) != 0x0;
        }
        this.m_lastBooleanBitFieldIndex = 0;
        this.m_lastBooleanBitFieldPosition = currentPosition + 1;
        this.m_lastBooleanBitFieldValue = this.m_byteBuffer.get();
        final int i = this.m_lastBooleanBitFieldValue & 0x80;
        return i != 0;
    }
    
    public final String readString() throws EOFException {
        int limit;
        int i;
        for (limit = this.m_byteBuffer.limit(), i = this.m_byteBuffer.position(); i < limit && this.m_byteBuffer.get(i) != 0; ++i) {}
        if (i >= limit) {
            throw new EOFException("Unable to find a valid Null terminated UTF-8 string end.");
        }
        final int length = i - this.m_byteBuffer.position();
        if (length > 0) {
            if (length > this.stringBuffer.length) {
                this.stringBuffer = new byte[length];
            }
            this.m_byteBuffer.get(this.stringBuffer, 0, length);
            this.m_byteBuffer.get();
            return StringUtils.fromUTF8(this.stringBuffer, 0, length);
        }
        this.m_byteBuffer.get();
        return "";
    }
    
    public final int getOffset() {
        return this.m_byteBuffer.position();
    }
    
    public final void setOffset(final int offset) throws IOException {
        this.m_byteBuffer.position(offset);
    }
    
    static {
        DEFAULT_ORDERING = ByteOrder.LITTLE_ENDIAN;
        m_logger = Logger.getLogger((Class)ExtendedDataInputStream.class);
        EMPTY_BYTE_BUFFER = ByteBuffer.allocate(0);
    }
}
