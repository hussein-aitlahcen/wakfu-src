package com.ankamagames.framework.net.download;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import java.util.concurrent.*;

public class AsynchronousDownloader
{
    public static final Logger m_logger;
    private final ExecutorService m_service;
    
    public AsynchronousDownloader() {
        this(-1);
    }
    
    public AsynchronousDownloader(final int maxConcurrentDownloads) {
        super();
        if (maxConcurrentDownloads < 1) {
            this.m_service = Executors.newCachedThreadPool();
        }
        else if (maxConcurrentDownloads == 1) {
            this.m_service = Executors.newSingleThreadExecutor();
        }
        else {
            this.m_service = Executors.newFixedThreadPool(maxConcurrentDownloads);
        }
    }
    
    public boolean awaitTermination(final long timeoutMS) {
        try {
            return this.m_service.awaitTermination(timeoutMS, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e) {
            return true;
        }
    }
    
    public void shutdown() {
        this.m_service.shutdown();
    }
    
    public void shutdownNow() {
        this.m_service.shutdownNow();
    }
    
    public Future<DownloadInfo> add(@NotNull final DownloadInfo downloadInfo) {
        return this.m_service.submit((Callable<DownloadInfo>)new DownloadTask(downloadInfo));
    }
    
    public Future<DownloadInfo> add(@NotNull final DownloadInfo downloadInfo, @NotNull final DownloadListener listener) {
        downloadInfo.setListener(listener);
        return this.add(downloadInfo);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AsynchronousDownloader.class);
    }
}
