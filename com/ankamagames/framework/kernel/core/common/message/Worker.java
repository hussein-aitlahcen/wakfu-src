package com.ankamagames.framework.kernel.core.common.message;

import org.apache.log4j.*;
import java.util.concurrent.locks.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import java.util.concurrent.*;

public final class Worker
{
    private static final Logger m_logger;
    static final String WORKER_NAME = "Worker";
    private static final Worker m_instance;
    private final Lock m_wakeUpMutex;
    private final Condition m_wakeUpCond;
    private WorkerThread m_workerThread;
    private final AtomicWorker m_worker;
    
    private Worker() {
        super();
        this.m_wakeUpMutex = new ReentrantLock();
        this.m_wakeUpCond = this.m_wakeUpMutex.newCondition();
        this.m_worker = new AtomicWorker();
    }
    
    public static Worker getInstance() {
        return Worker.m_instance;
    }
    
    public void start() {
        if (this.m_workerThread != null && this.m_workerThread.isRunning()) {
            Worker.m_logger.fatal((Object)"[PAS BIEN !!! Worker already running !!!");
            return;
        }
        (this.m_workerThread = new WorkerThread(new WorkerRunner(this.m_worker), "Worker")).start();
    }
    
    public void startInOpenGLThread() {
        if (this.m_workerThread != null && this.m_workerThread.isRunning()) {
            Worker.m_logger.fatal((Object)"[PAS BIEN !!! Worker already running !!!", (Throwable)new IllegalStateException("Worker already running"));
            return;
        }
        (this.m_workerThread = new WorkerThread(new WorkerRunner(new WorkerOGLRunner(this.m_worker)), "Worker")).start();
    }
    
    public void join() throws InterruptedException {
        if (this.m_workerThread == null) {
            Worker.m_logger.fatal((Object)"[PAS BIEN !!! Worker is not running !!!", (Throwable)new IllegalStateException("Worker is not running"));
            return;
        }
        this.m_workerThread.join();
    }
    
    public void wakeUp() {
        if (this.m_workerThread == null) {
            Worker.m_logger.fatal((Object)"[PAS BIEN !!! Worker is not running !!!", (Throwable)new IllegalStateException("Worker is not running"));
            return;
        }
        if (this.m_wakeUpMutex.tryLock()) {
            this.m_wakeUpCond.signalAll();
            this.m_wakeUpMutex.unlock();
        }
    }
    
    public void interrupt() {
        if (this.m_workerThread == null) {
            Worker.m_logger.fatal((Object)"[PAS BIEN !!! Worker is not running !!!", (Throwable)new IllegalStateException("Worker is not running"));
            return;
        }
        this.m_workerThread.interrupt();
        this.wakeUp();
    }
    
    public void kill() {
        if (this.m_workerThread == null) {
            Worker.m_logger.fatal((Object)"[PAS BIEN !!! Worker is not running !!!", (Throwable)new IllegalStateException("Worker is not running"));
            return;
        }
        Worker.m_logger.warn((Object)("Worker killed by " + ExceptionFormatter.currentStackTrace()));
        this.m_worker.clear();
        this.setRunning(false);
    }
    
    public void setRunning(final boolean running) {
        if (this.m_workerThread == null) {
            Worker.m_logger.fatal((Object)"[PAS BIEN !!! Worker is not running !!!", (Throwable)new IllegalStateException("Worker is not running"));
            return;
        }
        this.m_workerThread.setRunning(running);
        this.wakeUp();
    }
    
    public void pushMessage(final Message message) {
        if (message == null) {
            return;
        }
        message.setWorkerTimeStamp(System.currentTimeMillis());
        this.m_worker.offer(message);
        if (this.isRunning()) {
            this.wakeUp();
        }
    }
    
    public void pushMessages(final List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        final int count = messages.size();
        final long now = System.currentTimeMillis();
        for (int i = 0; i < count; ++i) {
            final Message message = messages.get(i);
            message.setWorkerTimeStamp(now);
            this.m_worker.offer(message);
        }
        if (this.isRunning()) {
            this.wakeUp();
        }
    }
    
    public int getWaitingMessageCount() {
        return this.m_worker.messageSize();
    }
    
    public boolean isRunning() {
        return this.m_workerThread != null && this.m_workerThread.isRunning();
    }
    
    public String getQueueSummary() {
        return this.m_worker.getQueueSummary();
    }
    
    static {
        m_logger = Logger.getLogger((Class)Worker.class);
        m_instance = new Worker();
    }
    
    private static class WorkerThread extends Thread
    {
        protected boolean m_running;
        
        WorkerThread(final Runnable runnable, final String name) {
            super(runnable, name);
        }
        
        public boolean isRunning() {
            return this.m_running;
        }
        
        public void setRunning(final boolean running) {
            this.m_running = running;
        }
        
        @Override
        public String toString() {
            return "WorkerThread{m_running=" + this.m_running + '}';
        }
    }
    
    private class WorkerRunner implements Runnable
    {
        private final Runnable m_runner;
        
        WorkerRunner(final Runnable runner) {
            super();
            this.m_runner = runner;
        }
        
        @Override
        public void run() {
            Worker.m_logger.info((Object)"Worker running");
            Worker.this.setRunning(true);
            while (Worker.this.isRunning()) {
                this.m_runner.run();
                final long timeToWait = ProcessScheduler.getInstance().getMaximalSleepTime();
                if (timeToWait <= 0L) {
                    continue;
                }
                if (!Worker.this.m_wakeUpMutex.tryLock()) {
                    continue;
                }
                try {
                    Worker.this.m_wakeUpCond.await(timeToWait, TimeUnit.MILLISECONDS);
                }
                catch (InterruptedException e) {
                    Worker.m_logger.error((Object)"Worker interrupted", (Throwable)e);
                }
                finally {
                    Worker.this.m_wakeUpMutex.unlock();
                }
            }
            Worker.m_logger.info((Object)"Worker stopped");
            Worker.this.m_workerThread = null;
        }
    }
}
