package com.ankamagames.framework.net.download;

import org.jetbrains.annotations.*;

public class DownloadableFieldProvider implements DownloadListener
{
    private final String m_finalURL;
    private final String m_field;
    private final DownloadableFieldProviderListener m_listener;
    private String m_url;
    
    public DownloadableFieldProvider(final String defaultURL, final String finalURL, final String field, final DownloadableFieldProviderListener listener) {
        super();
        this.m_url = defaultURL;
        this.m_finalURL = finalURL;
        this.m_field = field;
        this.m_listener = listener;
    }
    
    public DownloadableFieldProvider(final String url) {
        super();
        this.m_url = url;
        this.m_finalURL = null;
        this.m_field = null;
        this.m_listener = null;
    }
    
    @Override
    public void onDownloadStateChanged(@NotNull final DownloadInfo download) {
        if (download.getState() == DownloadState.FINISHED) {
            this.m_url = this.m_finalURL;
            this.m_listener.onDownloaded(this.m_field, download.getLocalToString());
        }
    }
    
    public String getUrl() {
        return this.m_url;
    }
}
