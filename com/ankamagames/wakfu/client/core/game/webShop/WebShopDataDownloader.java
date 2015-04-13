package com.ankamagames.wakfu.client.core.game.webShop;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.almanach.*;
import java.net.*;
import java.io.*;
import com.ankamagames.framework.net.download.*;

public class WebShopDataDownloader
{
    private static final Logger m_logger;
    public static final WebShopDataDownloader INSTANCE;
    private final AsynchronousDownloader m_downloader;
    
    public WebShopDataDownloader() {
        super();
        this.m_downloader = new AsynchronousDownloader(-1);
    }
    
    public void add(final String remoteUrl, final DownloadListener listener) {
        try {
            final File file = WebShopFileHelper.getCachedFilePathFromRemoteUrl(remoteUrl);
            final CacheValidator validator = new HttpFileCacheValidator(HTTPFileCacheManager.INSTANCE.getEtag(file.getName()));
            final DownloadInfo downloadInfo = new FileDownloadInfo(new URL(remoteUrl), file, true, true);
            downloadInfo.setCacheValidator(validator);
            this.m_downloader.add(downloadInfo, new CacheFillerListener(listener));
        }
        catch (MalformedURLException e) {
            WebShopDataDownloader.m_logger.warn((Object)e.getMessage());
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("WebShopDataDownloader");
        sb.append('}');
        return sb.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)WebShopDataDownloader.class);
        INSTANCE = new WebShopDataDownloader();
    }
}
