package com.ankamagames.framework.fileFormat.io.binaryStorage.stream;

import java.io.*;

final class BufferedDataInputStreamEx extends DataInputStream
{
    private final BufferedInputStreamEx m_buffer;
    
    public BufferedDataInputStreamEx() {
        super(null);
        final BufferedInputStreamEx bufferedInputStreamEx = new BufferedInputStreamEx(null);
        this.m_buffer = bufferedInputStreamEx;
        this.in = bufferedInputStreamEx;
    }
    
    public final void setStream(final InputStream istream) {
        this.m_buffer.setStream(istream);
    }
    
    @Override
    public void close() throws IOException {
        this.m_buffer.close();
    }
    
    private static class BufferedInputStreamEx extends BufferedInputStream
    {
        public BufferedInputStreamEx(final InputStream istream) {
            super(istream);
        }
        
        public BufferedInputStreamEx(final InputStream istream, final int size) {
            super(istream, size);
        }
        
        public void setStream(final InputStream istream) {
            this.in = istream;
            this.pos = 0;
            this.count = 0;
            this.marklimit = 0;
            this.markpos = -1;
            this.buf = new byte[8192];
        }
        
        @Override
        public void close() throws IOException {
            super.close();
        }
    }
}
