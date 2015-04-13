package com.ankamagames.framework.kernel.core.common.message;

import org.apache.log4j.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.profiling.*;

class WorkerOGLRunner implements Runnable
{
    private static final Logger m_logger;
    private final Runnable m_runner;
    
    WorkerOGLRunner(final Runnable runner) {
        super();
        this.m_runner = runner;
    }
    
    @Override
    public void run() {
        try {
            if (!Threading.isOpenGLThread()) {
                Threading.invokeOnOpenGLThread((Runnable)this);
                return;
            }
        }
        catch (GLException e) {
            Threading.invokeOnOpenGLThread((Runnable)this);
            WorkerOGLRunner.m_logger.error((Object)"Exception lev\u00e9e", (Throwable)e);
        }
        catch (Throwable e2) {
            Threading.invokeOnOpenGLThread((Runnable)this);
            WorkerOGLRunner.m_logger.error((Object)"Exception lev\u00e9e", e2);
            return;
        }
        Profiler.start("Worker", 1.0f, 0.0f, 0.0f);
        this.m_runner.run();
        Profiler.end();
    }
    
    @Override
    public String toString() {
        return "WorkerOGLRunner{m_runner=" + this.m_runner + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)WorkerOGLRunner.class);
    }
}
