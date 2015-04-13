package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.util.*;
import org.apache.tools.ant.*;

public class ExecuteWatchdog implements TimeoutObserver
{
    private Process process;
    private volatile boolean watch;
    private Exception caught;
    private volatile boolean killedProcess;
    private Watchdog watchdog;
    
    public ExecuteWatchdog(final long timeout) {
        super();
        this.watch = false;
        this.caught = null;
        this.killedProcess = false;
        (this.watchdog = new Watchdog(timeout)).addTimeoutObserver(this);
    }
    
    public ExecuteWatchdog(final int timeout) {
        this((long)timeout);
    }
    
    public synchronized void start(final Process process) {
        if (process == null) {
            throw new NullPointerException("process is null.");
        }
        if (this.process != null) {
            throw new IllegalStateException("Already running.");
        }
        this.caught = null;
        this.killedProcess = false;
        this.watch = true;
        this.process = process;
        this.watchdog.start();
    }
    
    public synchronized void stop() {
        this.watchdog.stop();
        this.cleanUp();
    }
    
    public synchronized void timeoutOccured(final Watchdog w) {
        try {
            try {
                this.process.exitValue();
            }
            catch (IllegalThreadStateException itse) {
                if (this.watch) {
                    this.killedProcess = true;
                    this.process.destroy();
                }
            }
        }
        catch (Exception e) {
            this.caught = e;
        }
        finally {
            this.cleanUp();
        }
    }
    
    protected synchronized void cleanUp() {
        this.watch = false;
        this.process = null;
    }
    
    public synchronized void checkException() throws BuildException {
        if (this.caught != null) {
            throw new BuildException("Exception in ExecuteWatchdog.run: " + this.caught.getMessage(), this.caught);
        }
    }
    
    public boolean isWatching() {
        return this.watch;
    }
    
    public boolean killedProcess() {
        return this.killedProcess;
    }
}
