package com.ankamagames.framework.fileFormat.io;

import org.apache.log4j.*;
import java.io.*;

public class BufferedRandomAccessReader implements Closeable
{
    protected static final Logger m_logger;
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    public static final int MAX_BUFFER_SIZE = 1048576;
    private final RandomAccessFile m_file;
    private final byte[] m_buffer;
    private final int m_bufferSize;
    private int m_cursor;
    private int m_bufferEnd;
    private long m_realPosition;
    
    public BufferedRandomAccessReader(final File f, final int bufferSize) throws IOException {
        super();
        this.m_file = new RandomAccessFile(f, "r");
        this.invalidate();
        if (bufferSize > 0 && bufferSize <= 1048576) {
            this.m_buffer = new byte[bufferSize];
            this.m_bufferSize = this.m_buffer.length;
            return;
        }
        throw new IOException("Invalid buffer size: " + bufferSize);
    }
    
    public BufferedRandomAccessReader(final File f) throws IOException {
        this(f, 1024);
    }
    
    @Override
    public final void close() throws IOException {
        this.m_file.close();
    }
    
    public final long length() throws IOException {
        return this.m_file.length();
    }
    
    public final int read() throws IOException {
        if (this.m_cursor >= this.m_bufferEnd && this.fillBuffer() < 0) {
            return -1;
        }
        if (this.m_bufferEnd == 0) {
            return -1;
        }
        return this.m_buffer[this.m_cursor++];
    }
    
    public final int read(final byte[] buffer) throws IOException {
        return this.read(buffer, 0, buffer.length);
    }
    
    public final int read(final byte[] buffer, final int offset, final int maxLength) throws IOException {
        if (this.m_cursor >= this.m_bufferEnd && this.fillBuffer() < 0) {
            return -1;
        }
        final int length = Math.min(maxLength, this.m_bufferEnd - this.m_cursor);
        System.arraycopy(this.m_buffer, this.m_cursor, buffer, offset, length);
        this.m_cursor += length;
        return length;
    }
    
    public final String readLine() throws IOException {
        if (this.m_cursor >= this.m_bufferEnd && this.fillBuffer() < 0) {
            return null;
        }
        int lineEnd = -1;
        for (int i = this.m_cursor; i < this.m_bufferEnd; ++i) {
            if (this.m_buffer[i] == 10) {
                lineEnd = i;
                break;
            }
        }
        if (lineEnd >= 0) {
            String result;
            if (lineEnd > 0 && this.m_buffer[lineEnd - 1] == 13) {
                result = new String(this.m_buffer, this.m_cursor, lineEnd - this.m_cursor - 1);
            }
            else {
                result = new String(this.m_buffer, this.m_cursor, lineEnd - this.m_cursor);
            }
            this.m_cursor = lineEnd + 1;
            return result;
        }
        final StringBuilder builder = new StringBuilder(100);
        int read;
        for (read = this.read(); read != -1 && read != 10; read = this.read()) {
            builder.append((char)read);
        }
        if (read == -1 && builder.length() == 0) {
            return null;
        }
        if (builder.charAt(builder.length() - 1) == '\r') {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }
    
    public final long getFilePointer() throws IOException {
        return this.m_realPosition - this.m_bufferEnd + this.m_cursor;
    }
    
    public final void seek(final long position) throws IOException {
        final int delta = (int)(this.m_realPosition - position);
        if (delta >= 0 && delta <= this.m_bufferEnd) {
            this.m_cursor = this.m_bufferEnd - delta;
        }
        else {
            this.m_file.seek(position);
            this.invalidate();
        }
    }
    
    private int fillBuffer() throws IOException {
        final int read = this.m_file.read(this.m_buffer, 0, this.m_bufferSize);
        if (read >= 0) {
            this.m_realPosition += read;
            this.m_bufferEnd = read;
            this.m_cursor = 0;
        }
        return read;
    }
    
    private void invalidate() throws IOException {
        this.m_bufferEnd = 0;
        this.m_cursor = 0;
        this.m_realPosition = this.m_file.getFilePointer();
    }
    
    static {
        m_logger = Logger.getLogger((Class)BufferedRandomAccessReader.class);
    }
}
