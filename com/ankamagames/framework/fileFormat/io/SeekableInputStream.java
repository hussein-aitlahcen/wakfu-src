package com.ankamagames.framework.fileFormat.io;

import java.io.*;

public interface SeekableInputStream
{
    int read() throws IOException;
    
    int read(byte[] p0) throws IOException;
    
    int read(byte[] p0, int p1, int p2) throws IOException;
    
    void seek(long p0) throws IOException;
    
    long length() throws IOException;
    
    long tell() throws IOException;
    
    void close() throws IOException;
}
