package com.ankamagames.framework.net.download;

import java.net.*;
import java.util.concurrent.atomic.*;
import org.jetbrains.annotations.*;
import java.io.*;

public abstract class DownloadInfo
{
    private final URL m_remoteURL;
    private AtomicReference<DownloadState> m_state;
    private DownloadListener m_listener;
    private CacheValidator m_cacheValidator;
    
    public DownloadInfo(@NotNull final URL remoteURL) {
        super();
        this.m_listener = null;
        this.m_cacheValidator = DefaultCacheValidator.VALIDATOR;
        this.m_remoteURL = remoteURL;
        this.m_state = new AtomicReference<DownloadState>(DownloadState.NOT_STARTED);
    }
    
    public void setListener(final DownloadListener listener) {
        this.m_listener = listener;
    }
    
    public void setCacheValidator(@NotNull final CacheValidator cacheValidator) {
        (this.m_cacheValidator = cacheValidator).setDownloadInfo(this);
    }
    
    public CacheValidator getCacheValidator() {
        return this.m_cacheValidator;
    }
    
    public boolean isCacheValid() {
        return this.m_cacheValidator != null && this.m_cacheValidator.isValid();
    }
    
    public DownloadState getState() {
        return this.m_state.get();
    }
    
    abstract OutputStream createOutputStream() throws IOException;
    
    abstract boolean closeStream(final boolean p0);
    
    void setState(final DownloadState state) {
        final DownloadState oldState = this.m_state.getAndSet(state);
        if (this.m_listener != null && oldState != state) {
            this.m_listener.onDownloadStateChanged(this);
        }
    }
    
    URL getRemoteURL() {
        return this.m_remoteURL;
    }
    
    protected abstract String getLocalToString();
    
    @Override
    public String toString() {
        return '{' + this.getClass().getSimpleName() + ' ' + this.m_state + " remote:" + this.m_remoteURL + " local:" + this.getLocalToString() + '}';
    }
}
