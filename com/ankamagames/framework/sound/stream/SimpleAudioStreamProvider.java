package com.ankamagames.framework.sound.stream;

import java.net.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class SimpleAudioStreamProvider implements AudioStreamProvider
{
    private final URL m_url;
    private InputStream m_stream;
    
    public SimpleAudioStreamProvider(final URL url) throws IOException {
        super();
        this.m_url = url;
    }
    
    @Override
    public void openStream() throws IOException {
        final InputStream stream = this.m_url.openStream();
        final StringBuilder type = new StringBuilder();
        for (int b = stream.read(); b != -1 && type.length() < 3; b = stream.read()) {
            type.append((char)b);
        }
        stream.close();
        final boolean crypted = !type.toString().toUpperCase().equals("OGG");
        if (crypted) {
            this.m_stream = new UncryptInputStream(this.m_url.openStream());
        }
        else {
            this.m_stream = this.m_url.openStream();
        }
    }
    
    @Override
    public void reset() throws IOException {
        if (this.m_stream != null) {
            this.m_stream.close();
        }
        this.openStream();
    }
    
    @Override
    public void close() throws IOException {
        if (this.m_stream != null) {
            this.m_stream.close();
            this.m_stream = null;
        }
    }
    
    @Override
    public boolean isSeekable() {
        return false;
    }
    
    @Override
    public void seek(final long offset) throws IOException {
    }
    
    @Override
    public long length() throws IOException {
        return 0L;
    }
    
    @Override
    public long tell() throws IOException {
        return 0L;
    }
    
    @Override
    public int read() throws IOException {
        return this.m_stream.read();
    }
    
    @Override
    public int read(final byte[] buffer) throws IOException {
        return this.m_stream.read(buffer);
    }
    
    @Override
    public int read(final byte[] buffer, final int offset, final int length) throws IOException {
        return this.m_stream.read(buffer, offset, length);
    }
    
    @Override
    public String getDescription() {
        return this.m_url.toString();
    }
    
    @Override
    public String getFileId() {
        return FileHelper.getNameWithoutExt(this.m_url.toString());
    }
}
