package com.ankamagames.framework.sound.stream;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class PakAudioStreamProvider implements AudioStreamProvider
{
    public SeekableInputStream m_stream;
    private final String m_description;
    private final PakFile m_file;
    private final String m_fileName;
    
    public PakAudioStreamProvider(final PakFile file, final String fileName, final String description) {
        super();
        this.m_description = description;
        this.m_file = file;
        this.m_fileName = fileName;
    }
    
    @Override
    public void openStream() throws IOException {
        final PakInputStream stream = this.m_file.openStream(this.m_fileName);
        final StringBuilder type = new StringBuilder();
        for (int b = stream.read(); b != -1 && type.length() < 3; b = stream.read()) {
            type.append((char)b);
        }
        stream.seek(0L);
        if (type.toString().toUpperCase().equals("OGG")) {
            this.m_stream = stream;
        }
        else {
            this.m_stream = new UncryptPakInputStream(stream);
        }
        this.reset();
    }
    
    @Override
    public void reset() throws IOException {
        this.m_stream.seek(0L);
    }
    
    @Override
    public void close() throws IOException {
        this.m_stream.close();
    }
    
    @Override
    public boolean isSeekable() {
        return true;
    }
    
    @Override
    public void seek(final long offset) throws IOException {
        this.m_stream.seek(offset);
    }
    
    @Override
    public long length() throws IOException {
        return this.m_stream.length();
    }
    
    @Override
    public long tell() throws IOException {
        return this.m_stream.tell();
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
        return this.m_description;
    }
    
    @Override
    public String getFileId() {
        return FileHelper.getNameWithoutExt(this.m_description);
    }
}
