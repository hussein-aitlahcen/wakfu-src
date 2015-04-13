package org.apache.tools.ant.taskdefs;

import java.lang.reflect.*;
import java.util.*;

class ProcessDestroyer implements Runnable
{
    private static final int THREAD_DIE_TIMEOUT = 20000;
    private HashSet processes;
    private Method addShutdownHookMethod;
    private Method removeShutdownHookMethod;
    private ProcessDestroyerImpl destroyProcessThread;
    private boolean added;
    private boolean running;
    
    ProcessDestroyer() {
        super();
        this.processes = new HashSet();
        this.destroyProcessThread = null;
        this.added = false;
        this.running = false;
        try {
            final Class[] paramTypes = { Thread.class };
            this.addShutdownHookMethod = Runtime.class.getMethod("addShutdownHook", (Class<?>[])paramTypes);
            this.removeShutdownHookMethod = Runtime.class.getMethod("removeShutdownHook", (Class<?>[])paramTypes);
        }
        catch (NoSuchMethodException e2) {}
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void addShutdownHook() {
        if (this.addShutdownHookMethod != null && !this.running) {
            this.destroyProcessThread = new ProcessDestroyerImpl();
            final Object[] args = { this.destroyProcessThread };
            try {
                this.addShutdownHookMethod.invoke(Runtime.getRuntime(), args);
                this.added = true;
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (InvocationTargetException e2) {
                final Throwable t = e2.getTargetException();
                if (t != null && t.getClass() == IllegalStateException.class) {
                    this.running = true;
                }
                else {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    private void removeShutdownHook() {
        if (this.removeShutdownHookMethod != null && this.added && !this.running) {
            final Object[] args = { this.destroyProcessThread };
            try {
                final Boolean removed = (Boolean)this.removeShutdownHookMethod.invoke(Runtime.getRuntime(), args);
                if (!removed) {
                    System.err.println("Could not remove shutdown hook");
                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (InvocationTargetException e2) {
                final Throwable t = e2.getTargetException();
                if (t != null && t.getClass() == IllegalStateException.class) {
                    this.running = true;
                }
                else {
                    e2.printStackTrace();
                }
            }
            this.destroyProcessThread.setShouldDestroy(false);
            if (!this.destroyProcessThread.getThreadGroup().isDestroyed()) {
                this.destroyProcessThread.start();
            }
            try {
                this.destroyProcessThread.join(20000L);
            }
            catch (InterruptedException ex) {}
            this.destroyProcessThread = null;
            this.added = false;
        }
    }
    
    public boolean isAddedAsShutdownHook() {
        return this.added;
    }
    
    public boolean add(final Process process) {
        synchronized (this.processes) {
            if (this.processes.size() == 0) {
                this.addShutdownHook();
            }
            return this.processes.add(process);
        }
    }
    
    public boolean remove(final Process process) {
        synchronized (this.processes) {
            final boolean processRemoved = this.processes.remove(process);
            if (processRemoved && this.processes.size() == 0) {
                this.removeShutdownHook();
            }
            return processRemoved;
        }
    }
    
    public void run() {
        synchronized (this.processes) {
            this.running = true;
            final Iterator e = this.processes.iterator();
            while (e.hasNext()) {
                e.next().destroy();
            }
        }
    }
    
    private class ProcessDestroyerImpl extends Thread
    {
        private boolean shouldDestroy;
        
        public ProcessDestroyerImpl() {
            super("ProcessDestroyer Shutdown Hook");
            this.shouldDestroy = true;
        }
        
        public void run() {
            if (this.shouldDestroy) {
                ProcessDestroyer.this.run();
            }
        }
        
        public void setShouldDestroy(final boolean shouldDestroy) {
            this.shouldDestroy = shouldDestroy;
        }
    }
}
