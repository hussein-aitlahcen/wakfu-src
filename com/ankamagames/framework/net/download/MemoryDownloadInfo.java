package com.ankamagames.framework.net.download;

import org.apache.log4j.*;
import java.net.*;
import org.jetbrains.annotations.*;
import java.io.*;

public class MemoryDownloadInfo extends DownloadInfo
{
    public static final Logger m_logger;
    private static final int DEFAULT_SIZE = 1024;
    private final ByteArrayOutputStream m_dataArray;
    
    public MemoryDownloadInfo(@NotNull final URL remoteURL) {
        super(remoteURL);
        this.m_dataArray = new ByteArrayOutputStream(1024);
    }
    
    @Override
    OutputStream createOutputStream() {
        return this.m_dataArray;
    }
    
    @Override
    boolean closeStream(final boolean success) {
        return success;
    }
    
    public byte[] getAsArray() {
        return this.m_dataArray.toByteArray();
    }
    
    public String getAsString() throws UnsupportedEncodingException {
        return this.m_dataArray.toString("UTF-8");
    }
    
    public String getAsString(final String charsetName) throws UnsupportedEncodingException {
        return this.m_dataArray.toString(charsetName);
    }
    
    @Override
    protected String getLocalToString() {
        return "Memory";
    }
    
    static {
        m_logger = Logger.getLogger((Class)MemoryDownloadInfo.class);
    }
}
