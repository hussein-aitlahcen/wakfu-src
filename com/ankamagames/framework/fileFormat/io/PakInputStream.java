package com.ankamagames.framework.fileFormat.io;

import java.io.*;

public class PakInputStream extends InputStream implements SeekableInputStream
{
    private final BufferedRandomAccessReader m_reader;
    private final long m_length;
    private final long m_start;
    
    public PakInputStream(final BufferedRandomAccessReader reader, final long startOffset, final long length) throws IOException {
        super();
        this.m_reader = reader;
        this.m_start = startOffset;
        this.m_length = length;
        reader.seek(startOffset);
    }
    
    @Override
    public int read() throws IOException {
        if (this.m_reader.getFilePointer() - this.m_start == this.m_length) {
            return -1;
        }
        return this.m_reader.read();
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        final long pos = this.m_reader.getFilePointer() - this.m_start;
        return this.m_reader.read(b, off, (int)Math.min(this.m_length - pos, len));
    }
    
    @Override
    public void seek(final long position) throws IOException {
        if (position < 0L) {
            throw new IOException("Position invalide dans le flux : " + position + ". Taille du fichier : " + this.m_length);
        }
        this.m_reader.seek(this.m_start + position);
    }
    
    @Override
    public long length() throws IOException {
        return this.m_length;
    }
    
    @Override
    public long tell() throws IOException {
        return this.m_reader.getFilePointer() - this.m_start;
    }
    
    @Override
    public void close() throws IOException {
        this.m_reader.close();
    }
    
    @Override
    public void mark(final int readlimit) {
    }
    
    @Override
    public void reset() throws IOException {
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public int available() throws IOException {
        return (int)this.m_length;
    }
}
