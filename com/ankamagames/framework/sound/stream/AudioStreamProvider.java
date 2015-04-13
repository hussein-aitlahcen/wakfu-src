package com.ankamagames.framework.sound.stream;

import java.io.*;

public interface AudioStreamProvider
{
    void openStream() throws IOException;
    
    void reset() throws IOException;
    
    void close() throws IOException;
    
    boolean isSeekable();
    
    void seek(long p0) throws IOException;
    
    long length() throws IOException;
    
    long tell() throws IOException;
    
    String getDescription();
    
    String getFileId();
    
    int read() throws IOException;
    
    int read(byte[] p0) throws IOException;
    
    int read(byte[] p0, int p1, int p2) throws IOException;
}
