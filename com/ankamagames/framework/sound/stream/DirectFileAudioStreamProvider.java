package com.ankamagames.framework.sound.stream;

import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class DirectFileAudioStreamProvider implements AudioStreamProvider
{
    private BufferedRandomAccessReader m_reader;
    private File m_file;
    private int m_bufferSize;
    
    public DirectFileAudioStreamProvider(final File f) throws IOException {
        this(f, -1);
    }
    
    public DirectFileAudioStreamProvider(final File f, final int bufferSize) throws IOException {
        super();
        this.m_file = f;
        this.m_bufferSize = bufferSize;
    }
    
    @Override
    public void openStream() throws IOException {
        if (this.m_bufferSize == -1) {
            this.m_reader = new BufferedRandomAccessReader(this.m_file);
        }
        else {
            this.m_reader = new BufferedRandomAccessReader(this.m_file, this.m_bufferSize);
        }
    }
    
    @Override
    public void reset() throws IOException {
        this.m_reader.seek(0L);
    }
    
    @Override
    public void close() throws IOException {
        if (this.m_reader != null) {
            this.m_reader.close();
        }
    }
    
    @Override
    public boolean isSeekable() {
        return true;
    }
    
    @Override
    public void seek(final long offset) throws IOException {
        this.m_reader.seek(offset);
    }
    
    @Override
    public long length() throws IOException {
        return this.m_reader.length();
    }
    
    @Override
    public long tell() throws IOException {
        return this.m_reader.getFilePointer();
    }
    
    @Override
    public String getDescription() {
        return this.m_file.getPath();
    }
    
    @Override
    public String getFileId() {
        return FileHelper.getNameWithoutExt(this.m_file.getPath());
    }
    
    @Override
    public int read() throws IOException {
        return this.m_reader.read();
    }
    
    @Override
    public int read(final byte[] buffer) throws IOException {
        return this.m_reader.read(buffer);
    }
    
    @Override
    public int read(final byte[] buffer, final int offset, final int length) throws IOException {
        return this.m_reader.read(buffer, offset, length);
    }
}
