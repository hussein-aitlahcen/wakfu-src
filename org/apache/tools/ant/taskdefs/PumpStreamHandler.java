package org.apache.tools.ant.taskdefs;

import java.io.*;

public class PumpStreamHandler implements ExecuteStreamHandler
{
    private Thread outputThread;
    private Thread errorThread;
    private Thread inputThread;
    private OutputStream out;
    private OutputStream err;
    private InputStream input;
    private final boolean nonBlockingRead;
    private static final long JOIN_TIMEOUT = 200L;
    
    public PumpStreamHandler(final OutputStream out, final OutputStream err, final InputStream input, final boolean nonBlockingRead) {
        super();
        this.out = out;
        this.err = err;
        this.input = input;
        this.nonBlockingRead = nonBlockingRead;
    }
    
    public PumpStreamHandler(final OutputStream out, final OutputStream err, final InputStream input) {
        this(out, err, input, false);
    }
    
    public PumpStreamHandler(final OutputStream out, final OutputStream err) {
        this(out, err, null);
    }
    
    public PumpStreamHandler(final OutputStream outAndErr) {
        this(outAndErr, outAndErr);
    }
    
    public PumpStreamHandler() {
        this(System.out, System.err);
    }
    
    public void setProcessOutputStream(final InputStream is) {
        this.createProcessOutputPump(is, this.out);
    }
    
    public void setProcessErrorStream(final InputStream is) {
        if (this.err != null) {
            this.createProcessErrorPump(is, this.err);
        }
    }
    
    public void setProcessInputStream(final OutputStream os) {
        if (this.input != null) {
            this.inputThread = this.createPump(this.input, os, true, this.nonBlockingRead);
        }
        else {
            try {
                os.close();
            }
            catch (IOException ex) {}
        }
    }
    
    public void start() {
        this.outputThread.start();
        this.errorThread.start();
        if (this.inputThread != null) {
            this.inputThread.start();
        }
    }
    
    public void stop() {
        this.finish(this.inputThread);
        try {
            this.err.flush();
        }
        catch (IOException ex) {}
        try {
            this.out.flush();
        }
        catch (IOException ex2) {}
        this.finish(this.outputThread);
        this.finish(this.errorThread);
    }
    
    protected final void finish(final Thread t) {
        if (t == null) {
            return;
        }
        try {
            StreamPumper s = null;
            if (t instanceof ThreadWithPumper) {
                s = ((ThreadWithPumper)t).getPumper();
            }
            if (s != null && s.isFinished()) {
                return;
            }
            if (!t.isAlive()) {
                return;
            }
            t.join(200L);
            if (s != null && !s.isFinished()) {
                s.stop();
            }
            while ((s == null || !s.isFinished()) && t.isAlive()) {
                t.interrupt();
                t.join(200L);
            }
        }
        catch (InterruptedException ex) {}
    }
    
    protected OutputStream getErr() {
        return this.err;
    }
    
    protected OutputStream getOut() {
        return this.out;
    }
    
    protected void createProcessOutputPump(final InputStream is, final OutputStream os) {
        this.outputThread = this.createPump(is, os);
    }
    
    protected void createProcessErrorPump(final InputStream is, final OutputStream os) {
        this.errorThread = this.createPump(is, os);
    }
    
    protected Thread createPump(final InputStream is, final OutputStream os) {
        return this.createPump(is, os, false);
    }
    
    protected Thread createPump(final InputStream is, final OutputStream os, final boolean closeWhenExhausted) {
        return this.createPump(is, os, closeWhenExhausted, true);
    }
    
    protected Thread createPump(final InputStream is, final OutputStream os, final boolean closeWhenExhausted, final boolean nonBlockingIO) {
        final StreamPumper pumper = new StreamPumper(is, os, closeWhenExhausted, nonBlockingIO);
        pumper.setAutoflush(true);
        final Thread result = new ThreadWithPumper(pumper);
        result.setDaemon(true);
        return result;
    }
    
    protected static class ThreadWithPumper extends Thread
    {
        private final StreamPumper pumper;
        
        public ThreadWithPumper(final StreamPumper p) {
            super(p);
            this.pumper = p;
        }
        
        protected StreamPumper getPumper() {
            return this.pumper;
        }
    }
}
