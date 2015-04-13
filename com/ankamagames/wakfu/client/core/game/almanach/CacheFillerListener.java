package com.ankamagames.wakfu.client.core.game.almanach;

import org.jetbrains.annotations.*;
import com.ankamagames.framework.net.download.*;
import java.io.*;

public class CacheFillerListener implements DownloadListener
{
    private final DownloadListener m_listener;
    
    public CacheFillerListener(final DownloadListener listener) {
        super();
        this.m_listener = listener;
    }
    
    @Override
    public void onDownloadStateChanged(@NotNull final DownloadInfo download) {
        if (download.getState() == DownloadState.FINISHED) {
            final FileDownloadInfo downloadInfo = (FileDownloadInfo)download;
            final HttpFileCacheValidator validator = (HttpFileCacheValidator)downloadInfo.getCacheValidator();
            final String remoteFileEtag = validator.getRemoteFileEtag();
            if (remoteFileEtag != null && !remoteFileEtag.equals(validator.getLocalFileEtag())) {
                final File file = downloadInfo.getAsFile();
                HTTPFileCacheManager.INSTANCE.put(file.getName(), remoteFileEtag);
            }
        }
        this.m_listener.onDownloadStateChanged(download);
    }
    
    @Override
    public String toString() {
        return "CacheFillerListener{}";
    }
}
