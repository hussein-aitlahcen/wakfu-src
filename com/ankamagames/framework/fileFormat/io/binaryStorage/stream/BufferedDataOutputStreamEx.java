package com.ankamagames.framework.fileFormat.io.binaryStorage.stream;

import java.io.*;

final class BufferedDataOutputStreamEx extends DataOutputStream
{
    private final BufferedOutputStreamEx m_buffer;
    
    BufferedDataOutputStreamEx() {
        super(null);
        final BufferedOutputStreamEx bufferedOutputStreamEx = new BufferedOutputStreamEx(null);
        this.m_buffer = bufferedOutputStreamEx;
        this.out = bufferedOutputStreamEx;
    }
    
    public final void setStream(final OutputStream ostream) {
        this.m_buffer.setStream(ostream);
        this.written = 0;
    }
    
    @Override
    public void close() throws IOException {
        this.m_buffer.close();
    }
    
    private static class BufferedOutputStreamEx extends BufferedOutputStream
    {
        BufferedOutputStreamEx(final OutputStream ostream) {
            super(ostream);
        }
        
        public void setStream(final OutputStream ostream) {
            this.out = ostream;
            this.count = 0;
        }
    }
}
