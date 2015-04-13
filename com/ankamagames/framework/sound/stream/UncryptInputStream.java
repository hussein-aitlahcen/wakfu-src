package com.ankamagames.framework.sound.stream;

import java.io.*;

public class UncryptInputStream extends InputStream
{
    private InputStream m_source;
    
    public UncryptInputStream(final InputStream stream) {
        super();
        this.m_source = stream;
    }
    
    @Override
    public int read() throws IOException {
        final int b = this.m_source.read();
        if (b == -1) {
            return -1;
        }
        return (b - 1 + 256) % 256;
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        final int read = this.m_source.read(b, off, len);
        for (int i = off, size = off + len; i < size; ++i) {
            b[i] = (byte)((b[i] - 1 + 256) % 256);
        }
        return read;
    }
    
    @Override
    public long skip(final long n) throws IOException {
        return this.m_source.skip(n);
    }
    
    @Override
    public int available() throws IOException {
        return this.m_source.available();
    }
    
    @Override
    public void close() throws IOException {
        this.m_source.close();
    }
    
    @Override
    public void mark(final int readlimit) {
        this.m_source.mark(readlimit);
    }
    
    @Override
    public void reset() throws IOException {
        this.m_source.reset();
    }
    
    @Override
    public boolean markSupported() {
        return this.m_source.markSupported();
    }
}
