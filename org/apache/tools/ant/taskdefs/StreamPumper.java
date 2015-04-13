package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.util.*;
import java.io.*;

public class StreamPumper implements Runnable
{
    private static final int SMALL_BUFFER_SIZE = 128;
    private final InputStream is;
    private final OutputStream os;
    private volatile boolean finish;
    private volatile boolean finished;
    private final boolean closeWhenExhausted;
    private boolean autoflush;
    private Exception exception;
    private int bufferSize;
    private boolean started;
    private final boolean useAvailable;
    private static final long POLL_INTERVAL = 100L;
    
    public StreamPumper(final InputStream is, final OutputStream os, final boolean closeWhenExhausted) {
        this(is, os, closeWhenExhausted, false);
    }
    
    public StreamPumper(final InputStream is, final OutputStream os, final boolean closeWhenExhausted, final boolean useAvailable) {
        super();
        this.autoflush = false;
        this.exception = null;
        this.bufferSize = 128;
        this.started = false;
        this.is = is;
        this.os = os;
        this.closeWhenExhausted = closeWhenExhausted;
        this.useAvailable = useAvailable;
    }
    
    public StreamPumper(final InputStream is, final OutputStream os) {
        this(is, os, false);
    }
    
    void setAutoflush(final boolean autoflush) {
        this.autoflush = autoflush;
    }
    
    public void run() {
        synchronized (this) {
            this.started = true;
        }
        this.finished = false;
        this.finish = false;
        final byte[] buf = new byte[this.bufferSize];
        try {
            while (true) {
                this.waitForInput(this.is);
                if (this.finish) {
                    break;
                }
                if (Thread.interrupted()) {
                    break;
                }
                final int length = this.is.read(buf);
                if (length <= 0 || this.finish) {
                    break;
                }
                if (Thread.interrupted()) {
                    break;
                }
                this.os.write(buf, 0, length);
                if (!this.autoflush) {
                    continue;
                }
                this.os.flush();
            }
            this.os.flush();
        }
        catch (InterruptedException ie) {}
        catch (Exception e) {
            synchronized (this) {
                this.exception = e;
            }
        }
        finally {
            if (this.closeWhenExhausted) {
                FileUtils.close(this.os);
            }
            this.finished = true;
            synchronized (this) {
                this.notifyAll();
            }
        }
    }
    
    public boolean isFinished() {
        return this.finished;
    }
    
    public synchronized void waitFor() throws InterruptedException {
        while (!this.isFinished()) {
            this.wait();
        }
    }
    
    public synchronized void setBufferSize(final int bufferSize) {
        if (this.started) {
            throw new IllegalStateException("Cannot set buffer size on a running StreamPumper");
        }
        this.bufferSize = bufferSize;
    }
    
    public synchronized int getBufferSize() {
        return this.bufferSize;
    }
    
    public synchronized Exception getException() {
        return this.exception;
    }
    
    synchronized void stop() {
        this.finish = true;
        this.notifyAll();
    }
    
    private void waitForInput(final InputStream is) throws IOException, InterruptedException {
        if (this.useAvailable) {
            while (!this.finish && is.available() == 0) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                synchronized (this) {
                    this.wait(100L);
                }
            }
        }
    }
}
