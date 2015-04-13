package com.ankamagames.framework.kernel.core.io;

import org.apache.log4j.*;
import java.util.concurrent.*;
import java.io.*;
import java.net.*;

public class AsyncLoader extends Thread
{
    private static final Logger m_logger;
    private boolean m_isAlive;
    private AsyncURL m_asyncURL;
    private final BlockingQueue<AsyncURL> m_urls;
    private static final AsyncLoader m_instance;
    
    private AsyncLoader() {
        super();
        this.m_urls = new LinkedBlockingQueue<AsyncURL>();
        this.m_isAlive = true;
        this.setName("AsyncLoader");
    }
    
    public static AsyncLoader getInstance() {
        return AsyncLoader.m_instance;
    }
    
    @Override
    public final void run() {
        while (this.m_isAlive) {
            if (this.m_asyncURL == null) {
                try {
                    this.m_asyncURL = this.m_urls.poll(1L, TimeUnit.SECONDS);
                }
                catch (InterruptedException e2) {
                    continue;
                }
                if (this.m_asyncURL == null) {
                    continue;
                }
            }
            try {
                this.m_asyncURL.stream();
                if (this.m_asyncURL.isReady()) {
                    this.m_asyncURL = null;
                }
            }
            catch (IOException e) {
                AsyncLoader.m_logger.error((Object)("An error occurs while streaming the url " + this.m_asyncURL.getURL().getPath()), (Throwable)e);
                this.m_asyncURL.failed();
                this.m_asyncURL = null;
            }
            Thread.yield();
        }
    }
    
    public final AsyncURL load(final URL url) {
        final AsyncURL asyncURL = new AsyncURL(url);
        this.m_urls.add(asyncURL);
        return asyncURL;
    }
    
    public final void kill() {
        this.m_isAlive = false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AsyncLoader.class);
        m_instance = new AsyncLoader();
    }
}
