package org.apache.tools.zip;

import java.nio.*;
import java.io.*;

class FallbackZipEncoding implements ZipEncoding
{
    private final String charset;
    
    public FallbackZipEncoding() {
        super();
        this.charset = null;
    }
    
    public FallbackZipEncoding(final String charset) {
        super();
        this.charset = charset;
    }
    
    public boolean canEncode(final String name) {
        return true;
    }
    
    public ByteBuffer encode(final String name) throws IOException {
        if (this.charset == null) {
            return ByteBuffer.wrap(name.getBytes());
        }
        return ByteBuffer.wrap(name.getBytes(this.charset));
    }
    
    public String decode(final byte[] data) throws IOException {
        if (this.charset == null) {
            return new String(data);
        }
        return new String(data, this.charset);
    }
}
