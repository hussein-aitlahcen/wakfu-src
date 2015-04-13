package com.ankamagames.framework.net.download;

import org.jetbrains.annotations.*;

public abstract class CacheValidator<D extends DownloadInfo>
{
    protected D m_downloadInfo;
    
    public void setDownloadInfo(@NotNull final D downloadInfo) {
        this.m_downloadInfo = downloadInfo;
    }
    
    public abstract boolean isValid();
}
