package com.ankamagames.framework.fileFormat.news;

import org.apache.log4j.*;
import java.net.*;
import org.json.*;
import com.ankamagames.framework.net.download.*;
import java.util.concurrent.*;
import java.util.*;
import org.jetbrains.annotations.*;
import java.io.*;

class NewsLoadingThread extends Thread implements NewsImageFactory
{
    public static final Logger m_logger;
    private final NewsLoadingListener m_listener;
    @NotNull
    private final URL m_streamURL;
    @NotNull
    private final File m_downloadDirectory;
    @NotNull
    private final AsynchronousDownloader m_downloader;
    private final ArrayList<NewsImage> m_imagesToLoad;
    
    NewsLoadingThread(@NotNull final URL streamURL, final File downloadDirectory, final NewsLoadingListener listener) {
        super();
        this.m_imagesToLoad = new ArrayList<NewsImage>();
        this.setDaemon(true);
        this.m_streamURL = streamURL;
        this.m_downloadDirectory = downloadDirectory;
        this.m_listener = listener;
        this.m_downloader = new AsynchronousDownloader();
    }
    
    @Override
    public void run() {
        final MemoryDownloadInfo streamDefDownload = new MemoryDownloadInfo(this.m_streamURL);
        final Future<DownloadInfo> streamDefFuture = this.m_downloader.add(streamDefDownload);
        try {
            streamDefFuture.get();
        }
        catch (Exception e) {
            NewsLoadingThread.m_logger.error((Object)"Error while downloading news stream", (Throwable)e);
            this.m_downloader.shutdownNow();
            if (this.m_listener != null) {
                this.m_listener.onNewsChannelLoaded(null);
            }
            return;
        }
        if (streamDefDownload.getState() != DownloadState.FINISHED) {
            NewsLoadingThread.m_logger.error((Object)("Error while downloading news stream, bad state" + streamDefDownload));
            if (this.m_listener != null) {
                this.m_listener.onNewsChannelLoaded(null);
            }
            this.m_downloader.shutdownNow();
            return;
        }
        String streamStr;
        try {
            streamStr = streamDefDownload.getAsString();
        }
        catch (UnsupportedEncodingException e2) {
            NewsLoadingThread.m_logger.error((Object)"Error while parsing news stream : bad encoding", (Throwable)e2);
            if (this.m_listener != null) {
                this.m_listener.onNewsChannelLoaded(null);
            }
            this.m_downloader.shutdownNow();
            return;
        }
        NewsChannel newsChannel;
        try {
            newsChannel = NewsJSONParser.parse(streamStr, this);
        }
        catch (JSONException e3) {
            NewsLoadingThread.m_logger.error((Object)"Error while parsing news stream : bad format", (Throwable)e3);
            if (this.m_listener != null) {
                this.m_listener.onNewsChannelLoaded(null);
            }
            this.m_downloader.shutdownNow();
            return;
        }
        this.m_listener.onNewsChannelLoaded(newsChannel);
        if (this.m_imagesToLoad.isEmpty()) {
            if (this.m_listener != null) {
                this.m_listener.onNewsImagesLoaded();
            }
        }
        else {
            newsChannel.setImagesToLoadCount(this.m_imagesToLoad.size());
            final DownloadListener imgDownloadListener = new DownloadListener() {
                @Override
                public void onDownloadStateChanged(@NotNull final DownloadInfo download) {
                    switch (download.getState()) {
                        case ERROR:
                        case FINISHED: {
                            final int remaining = newsChannel.decrementImagesToLoadCount();
                            if (remaining != 0) {
                                break;
                            }
                            NewsLoadingThread.this.m_downloader.shutdown();
                            if (NewsLoadingThread.this.m_listener != null) {
                                NewsLoadingThread.this.m_listener.onNewsImagesLoaded();
                                break;
                            }
                            break;
                        }
                    }
                }
            };
            for (final NewsImage image : this.m_imagesToLoad) {
                this.m_downloader.add(new FileDownloadInfo(image.getRemoteURL(), image.getFile(), true, true), imgDownloadListener);
            }
        }
    }
    
    boolean awaitTermination(final long timeoutMS) {
        return this.m_downloader.awaitTermination(timeoutMS);
    }
    
    @Override
    public NewsImage createImage(@Nullable final URL imageURL, @Nullable final String imageGuid, final long lastModificationDate) {
        if (imageURL == null) {
            return null;
        }
        final String timestamp = Long.toHexString(lastModificationDate);
        File localFile = null;
        if (imageGuid != null && imageGuid.length() > 0) {
            String extension = "";
            final String remoteFile = imageURL.getFile().toLowerCase();
            if (remoteFile.endsWith(".jpg") || remoteFile.endsWith(".jpeg")) {
                extension = ".jpg";
            }
            else if (remoteFile.endsWith(".png")) {
                extension = ".png";
            }
            localFile = new File(this.m_downloadDirectory, imageGuid + '_' + timestamp + extension);
            if (!this.checkFileValidity(localFile)) {
                localFile = null;
            }
        }
        if (localFile == null) {
            final String fileName = new File(imageURL.getFile()).getName();
            final int extensionPos = fileName.lastIndexOf(".");
            String localFilename;
            if (extensionPos == -1) {
                localFilename = fileName + '_' + timestamp;
            }
            else {
                localFilename = fileName.substring(0, extensionPos) + '_' + timestamp + fileName.substring(extensionPos);
            }
            localFile = new File(this.m_downloadDirectory, localFilename);
            if (!this.checkFileValidity(localFile)) {
                NewsLoadingThread.m_logger.error((Object)("Unable to get a valid local file for remote file " + imageURL + " (" + localFilename + ")"));
                return null;
            }
        }
        final NewsImage newsImage = new NewsImage(imageURL, localFile);
        if (!localFile.exists() || localFile.length() == 0L) {
            this.m_imagesToLoad.add(newsImage);
        }
        return newsImage;
    }
    
    private boolean checkFileValidity(final File imageFile) {
        if (!imageFile.exists()) {
            final File parentDirectory = imageFile.getParentFile();
            if (!parentDirectory.exists()) {
                if (!parentDirectory.mkdirs()) {
                    NewsLoadingThread.m_logger.error((Object)("Unable to create parent directories for local file " + imageFile));
                    return false;
                }
            }
            else if (!parentDirectory.isDirectory()) {
                NewsLoadingThread.m_logger.error((Object)("Unable to create file : parent directory is not a directory : " + parentDirectory));
                return false;
            }
            try {
                imageFile.createNewFile();
            }
            catch (IOException e) {
                NewsLoadingThread.m_logger.error((Object)("Unable to create local file " + imageFile), (Throwable)e);
                return false;
            }
            imageFile.delete();
            return true;
        }
        if (!imageFile.isFile()) {
            NewsLoadingThread.m_logger.error((Object)("Invalid local file : " + imageFile + " is a directory"));
            return false;
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NewsLoadingThread.class);
    }
    
    interface NewsLoadingListener
    {
        void onNewsChannelLoaded(NewsChannel p0);
        
        void onNewsImagesLoaded();
    }
}
