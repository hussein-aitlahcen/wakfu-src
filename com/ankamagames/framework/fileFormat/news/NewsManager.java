package com.ankamagames.framework.fileFormat.news;

import org.apache.log4j.*;
import java.net.*;
import java.io.*;

public class NewsManager implements NewsLoadingThread.NewsLoadingListener
{
    public static final Logger m_logger;
    public static final NewsManager INSTANCE;
    private NewsLoadingThread m_downloadThread;
    private URL m_lastNewsURL;
    private File m_lastCacheDirectory;
    private State m_state;
    private NewsChannel m_channel;
    
    private NewsManager() {
        super();
        this.m_lastNewsURL = null;
        this.m_lastCacheDirectory = null;
        this.m_state = State.NOT_LOADED;
        this.m_channel = null;
        this.m_downloadThread = null;
        this.m_state = State.NOT_LOADED;
    }
    
    public State load(final URL newsURL, final String newsCacheDirectory) {
        if (this.m_downloadThread != null && this.m_downloadThread.isAlive()) {
            NewsManager.m_logger.error((Object)"News are already downloading (thread still running)");
            return this.m_state = State.ERROR;
        }
        File downloadDirectory;
        try {
            downloadDirectory = new File(newsCacheDirectory).getCanonicalFile();
        }
        catch (IOException e) {
            NewsManager.m_logger.error((Object)("Invalid news cached directory : " + new File(newsCacheDirectory)), (Throwable)e);
            return this.m_state = State.ERROR;
        }
        if (newsURL == this.m_lastNewsURL && this.m_lastCacheDirectory == downloadDirectory) {
            return this.m_state;
        }
        if (!this.checkPaths(newsURL, downloadDirectory)) {
            return this.m_state = State.ERROR;
        }
        this.m_downloadThread = new NewsLoadingThread(newsURL, downloadDirectory, this);
        this.m_state = State.LOADING_STREAM;
        this.m_downloadThread.start();
        return this.m_state;
    }
    
    public State getState() {
        return this.m_state;
    }
    
    public boolean awaitTermination(final long timeoutMS) {
        return (this.m_state == State.LOADING_IMAGES || this.m_state == State.LOADING_STREAM) && this.m_downloadThread.awaitTermination(timeoutMS);
    }
    
    @Override
    public void onNewsChannelLoaded(final NewsChannel newsChannel) {
        if (newsChannel != null) {
            this.m_channel = newsChannel;
            this.m_state = State.LOADING_IMAGES;
        }
        else {
            this.m_state = State.ERROR;
            this.m_downloadThread = null;
        }
    }
    
    @Override
    public void onNewsImagesLoaded() {
        if (this.m_state != State.LOADING_IMAGES) {
            NewsManager.m_logger.error((Object)"We receive an 'images loaded' event, but we are not expecting it");
        }
        this.m_state = State.FULLY_LOADED;
    }
    
    private boolean checkPaths(final URL newsURL, final File cacheDirectory) {
        if (newsURL == null || cacheDirectory == null) {
            NewsManager.m_logger.error((Object)"No News URL or no cache directory : NewsManager not succesfully initialized");
            return false;
        }
        if (!cacheDirectory.exists()) {
            if (!cacheDirectory.mkdirs()) {
                NewsManager.m_logger.error((Object)("Unable to create news cache directory : " + cacheDirectory));
                return false;
            }
        }
        else if (!cacheDirectory.isDirectory()) {
            NewsManager.m_logger.error((Object)("Unable to create news cache directory : " + cacheDirectory + " : a file with this name already exists"));
            return false;
        }
        return true;
    }
    
    public NewsChannel getChannel() {
        return this.m_channel;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NewsManager.class);
        INSTANCE = new NewsManager();
    }
    
    public enum State
    {
        NOT_LOADED, 
        LOADING_STREAM, 
        LOADING_IMAGES, 
        ERROR, 
        FULLY_LOADED;
    }
}
