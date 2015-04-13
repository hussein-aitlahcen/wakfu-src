package com.ankamagames.wakfu.client.core.game.almanach;

import org.apache.log4j.*;
import com.ankamagames.framework.net.download.*;
import java.net.*;
import java.io.*;

public class AlmanachDataDownloader
{
    private static final Logger m_logger;
    public static final AlmanachDataDownloader INSTANCE;
    private final AsynchronousDownloader m_downloader;
    
    public AlmanachDataDownloader() {
        super();
        this.m_downloader = new AsynchronousDownloader(-1);
    }
    
    public void add(final String remoteUrl, final DownloadListener listener) {
        try {
            final File file = AlmanachFileHelper.getCachedFilePathFromRemoteUrl(remoteUrl);
            final HttpFileCacheValidator validator = new HttpFileCacheValidator(HTTPFileCacheManager.INSTANCE.getEtag(file.getName()));
            final FileDownloadInfo downloadInfo = new FileDownloadInfo(new URL(remoteUrl), file, true, true);
            downloadInfo.setCacheValidator(validator);
            this.m_downloader.add(downloadInfo, new CacheFillerListener(listener));
        }
        catch (MalformedURLException e) {
            AlmanachDataDownloader.m_logger.warn((Object)e.getMessage());
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AlmanachDataDownloader");
        sb.append('}');
        return sb.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AlmanachDataDownloader.class);
        INSTANCE = new AlmanachDataDownloader();
    }
}
