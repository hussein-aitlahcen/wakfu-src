package com.ankamagames.framework.kernel.core.io;

import java.net.*;
import java.io.*;

public class AsyncURL
{
    private static final byte[] BUFFER;
    private URL m_url;
    private InputStream m_stream;
    private byte[] m_data;
    private int m_dataRead;
    private boolean m_isReady;
    private boolean m_hasFailed;
    
    public AsyncURL(final URL url) {
        super();
        this.m_url = url;
        this.m_isReady = false;
        this.m_hasFailed = false;
    }
    
    public void stream() throws IOException {
        assert !this.m_isReady : "Stream must not be call if the file is already loaded";
        assert !this.m_hasFailed : "Stream must not be call if the file loading has failed";
        try {
            if (this.m_stream == null) {
                this.m_stream = new BufferedInputStream(this.m_url.openStream());
                this.m_dataRead = 0;
            }
            final int readBytes = this.m_stream.read(AsyncURL.BUFFER, 0, AsyncURL.BUFFER.length);
            if (readBytes == -1) {
                this.m_stream.close();
                this.m_isReady = true;
            }
            else {
                final byte[] data = new byte[this.m_dataRead + readBytes];
                if (this.m_data != null) {
                    System.arraycopy(this.m_data, 0, data, 0, this.m_dataRead);
                }
                System.arraycopy(AsyncURL.BUFFER, 0, data, this.m_dataRead, readBytes);
                this.m_data = data;
                this.m_dataRead += readBytes;
            }
        }
        catch (IOException e) {
            if (this.m_stream != null) {
                this.m_stream.close();
            }
            this.m_hasFailed = true;
            throw e;
        }
    }
    
    void failed() {
        this.m_hasFailed = true;
    }
    
    public final boolean isReady() {
        return this.m_isReady;
    }
    
    public final boolean hasFailed() {
        return this.m_hasFailed;
    }
    
    public final URL getURL() {
        return this.m_url;
    }
    
    public final byte[] getData() {
        return this.m_data;
    }
    
    @Override
    public String toString() {
        return (this.m_url == null) ? "null" : (this.m_url.toString() + " ready=" + this.m_isReady);
    }
    
    static {
        BUFFER = new byte[1048576];
    }
}
