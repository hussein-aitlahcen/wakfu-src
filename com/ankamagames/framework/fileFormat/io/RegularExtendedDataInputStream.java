package com.ankamagames.framework.fileFormat.io;

import java.nio.*;
import java.io.*;

public class RegularExtendedDataInputStream extends InputStream
{
    private ExtendedDataInputStream m_stream;
    
    public RegularExtendedDataInputStream(final ExtendedDataInputStream stream) {
        super();
        this.m_stream = stream;
    }
    
    @Override
    public int read() throws IOException {
        try {
            return this.m_stream.readByte();
        }
        catch (BufferUnderflowException e) {
            return -1;
        }
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        final int readBytes = this.m_stream.readBytes(b, off, len);
        return (readBytes == 0) ? -1 : readBytes;
    }
    
    @Override
    public void close() throws IOException {
        this.m_stream.close();
    }
    
    @Override
    public int available() throws IOException {
        return this.m_stream.available();
    }
}
