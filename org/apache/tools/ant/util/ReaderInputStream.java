package org.apache.tools.ant.util;

import java.io.*;

public class ReaderInputStream extends InputStream
{
    private static final int BYTE_MASK = 255;
    private Reader in;
    private String encoding;
    private byte[] slack;
    private int begin;
    
    public ReaderInputStream(final Reader reader) {
        super();
        this.encoding = System.getProperty("file.encoding");
        this.in = reader;
    }
    
    public ReaderInputStream(final Reader reader, final String encoding) {
        this(reader);
        if (encoding == null) {
            throw new IllegalArgumentException("encoding must not be null");
        }
        this.encoding = encoding;
    }
    
    public synchronized int read() throws IOException {
        if (this.in == null) {
            throw new IOException("Stream Closed");
        }
        byte result;
        if (this.slack != null && this.begin < this.slack.length) {
            result = this.slack[this.begin];
            if (++this.begin == this.slack.length) {
                this.slack = null;
            }
        }
        else {
            final byte[] buf = { 0 };
            if (this.read(buf, 0, 1) <= 0) {
                return -1;
            }
            result = buf[0];
        }
        return result & 0xFF;
    }
    
    public synchronized int read(final byte[] b, final int off, int len) throws IOException {
        if (this.in == null) {
            throw new IOException("Stream Closed");
        }
        if (len == 0) {
            return 0;
        }
        while (this.slack == null) {
            final char[] buf = new char[len];
            final int n = this.in.read(buf);
            if (n == -1) {
                return -1;
            }
            if (n <= 0) {
                continue;
            }
            this.slack = new String(buf, 0, n).getBytes(this.encoding);
            this.begin = 0;
        }
        if (len > this.slack.length - this.begin) {
            len = this.slack.length - this.begin;
        }
        System.arraycopy(this.slack, this.begin, b, off, len);
        this.begin += len;
        if (this.begin >= this.slack.length) {
            this.slack = null;
        }
        return len;
    }
    
    public synchronized void mark(final int limit) {
        try {
            this.in.mark(limit);
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }
    }
    
    public synchronized int available() throws IOException {
        if (this.in == null) {
            throw new IOException("Stream Closed");
        }
        if (this.slack != null) {
            return this.slack.length - this.begin;
        }
        if (this.in.ready()) {
            return 1;
        }
        return 0;
    }
    
    public boolean markSupported() {
        return false;
    }
    
    public synchronized void reset() throws IOException {
        if (this.in == null) {
            throw new IOException("Stream Closed");
        }
        this.slack = null;
        this.in.reset();
    }
    
    public synchronized void close() throws IOException {
        if (this.in != null) {
            this.in.close();
            this.slack = null;
            this.in = null;
        }
    }
}
