package com.ankamagames.framework.net.download;

import java.io.*;
import java.net.*;

public class HttpFileCacheValidator extends CacheValidator<FileDownloadInfo>
{
    public static final String ETAG_TAG = "ETag";
    public static final String LAST_MODIFIED_TAG = "Last-Modified";
    private String m_localFileEtag;
    private String m_remoteFileEtag;
    
    public HttpFileCacheValidator(final String localFileEtag) {
        super();
        this.m_localFileEtag = localFileEtag;
    }
    
    @Override
    public boolean isValid() {
        try {
            final URLConnection conn = ((FileDownloadInfo)this.m_downloadInfo).getRemoteURL().openConnection();
            String remoteFileEtag = null;
            int i = 0;
            while (true) {
                final String name = conn.getHeaderFieldKey(i);
                final String value = conn.getHeaderField(i);
                if (name == null && value == null) {
                    break;
                }
                if ("ETag".equals(name)) {
                    remoteFileEtag = value;
                    break;
                }
                if ("Last-Modified".equals(name)) {
                    remoteFileEtag = value;
                }
                ++i;
            }
            this.m_remoteFileEtag = remoteFileEtag;
            return this.m_remoteFileEtag != null && this.m_remoteFileEtag.equals(this.m_localFileEtag);
        }
        catch (IOException e) {
            return false;
        }
    }
    
    public String getLocalFileEtag() {
        return this.m_localFileEtag;
    }
    
    public String getRemoteFileEtag() {
        return this.m_remoteFileEtag;
    }
}
