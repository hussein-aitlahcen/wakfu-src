package com.ankamagames.baseImpl.common.clientAndServer.utils;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.longKey.*;
import java.util.concurrent.*;

public class AsyncMapLoader
{
    private static final Logger m_logger;
    private final ExecutorService m_loader;
    private final LongObjectLightWeightMap<Future> m_loading;
    
    public AsyncMapLoader(final String name) {
        this(name, 25);
    }
    
    public AsyncMapLoader(final String name, final int capacity) {
        super();
        this.m_loader = Executors.newFixedThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable r) {
                return new Thread(r, name);
            }
        });
        this.m_loading = new LongObjectLightWeightMap<Future>(capacity);
    }
    
    public void submit(final long key, final Runnable runnable) {
        final Future future = this.m_loader.submit(new Runnable() {
            @Override
            public void run() {
                runnable.run();
                AsyncMapLoader.this.m_loading.remove(key);
            }
        });
        this.m_loading.put(key, future);
    }
    
    public final void waitFor(final long key) {
        final Future future = this.m_loading.get(key);
        if (future == null) {
            return;
        }
        while (!future.isDone()) {
            try {
                Thread.sleep(3L);
            }
            catch (InterruptedException e) {
                AsyncMapLoader.m_logger.error((Object)"", (Throwable)e);
            }
        }
    }
    
    public final void remove(final long key) {
        final Future future = this.m_loading.remove(key);
        if (future != null) {
            future.cancel(true);
        }
    }
    
    public final void clear() {
        for (int i = 0; i < this.m_loading.size(); ++i) {
            final Future value = this.m_loading.getQuickValue(i);
            if (value != null) {
                value.cancel(true);
            }
        }
        this.m_loading.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AsyncMapLoader.class);
    }
}
