package org.apache.tools.ant.util;

import java.util.*;

public class Watchdog implements Runnable
{
    private Vector observers;
    private long timeout;
    private volatile boolean stopped;
    public static final String ERROR_INVALID_TIMEOUT = "timeout less than 1.";
    
    public Watchdog(final long timeout) {
        super();
        this.observers = new Vector(1);
        this.timeout = -1L;
        this.stopped = false;
        if (timeout < 1L) {
            throw new IllegalArgumentException("timeout less than 1.");
        }
        this.timeout = timeout;
    }
    
    public void addTimeoutObserver(final TimeoutObserver to) {
        this.observers.addElement(to);
    }
    
    public void removeTimeoutObserver(final TimeoutObserver to) {
        this.observers.removeElement(to);
    }
    
    protected final void fireTimeoutOccured() {
        final Enumeration e = this.observers.elements();
        while (e.hasMoreElements()) {
            e.nextElement().timeoutOccured(this);
        }
    }
    
    public synchronized void start() {
        this.stopped = false;
        final Thread t = new Thread(this, "WATCHDOG");
        t.setDaemon(true);
        t.start();
    }
    
    public synchronized void stop() {
        this.stopped = true;
        this.notifyAll();
    }
    
    public synchronized void run() {
        long now = System.currentTimeMillis();
        final long until = now + this.timeout;
        try {
            while (!this.stopped && until > now) {
                this.wait(until - now);
                now = System.currentTimeMillis();
            }
        }
        catch (InterruptedException ex) {}
        if (!this.stopped) {
            this.fireTimeoutOccured();
        }
    }
}
