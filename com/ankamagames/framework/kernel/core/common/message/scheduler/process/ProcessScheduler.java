package com.ankamagames.framework.kernel.core.common.message.scheduler.process;

import org.apache.log4j.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import java.util.*;

public class ProcessScheduler
{
    private static final Logger m_logger;
    private static final ProcessScheduler m_instance;
    private static final int DEFAULT_SLEEP_TIME = 30;
    private final ConcurrentLinkedQueue<SchedulerOperation> m_operations;
    private final List<ScheduledProcess> m_processes;
    private final List<ScheduledProcess> m_immediateReschedule;
    private final AtomicInteger m_numPendingSchedules;
    private final AtomicInteger m_numPendingOperations;
    
    private ProcessScheduler() {
        super();
        this.m_operations = new ConcurrentLinkedQueue<SchedulerOperation>();
        this.m_processes = new ArrayList<ScheduledProcess>();
        this.m_immediateReschedule = new ArrayList<ScheduledProcess>();
        this.m_numPendingSchedules = new AtomicInteger(0);
        this.m_numPendingOperations = new AtomicInteger(0);
    }
    
    public static ProcessScheduler getInstance() {
        return ProcessScheduler.m_instance;
    }
    
    public void schedule(final Runnable runnable) {
        this.schedule(runnable, 1L, 1);
    }
    
    public void schedule(final Runnable runnable, final long delay) {
        this.schedule(runnable, delay, -1);
    }
    
    public void scheduleOnTimeEvent(final Runnable runnable, final TimeInterval intervalUnit, final long intervalCount, final int repeatCount) {
        if (runnable == null) {
            ProcessScheduler.m_logger.error((Object)"Tentative d'insertion d'un Runnable null");
            return;
        }
        if (repeatCount == 0) {
            ProcessScheduler.m_logger.warn((Object)"On schedule un runnable pour \u00eatre execut\u00e9 0 fois, WTF ? (Gros blaireau)");
            return;
        }
        final ScheduledProcess scheduledProcess = new ScheduledProcessOnFixedEvent(runnable, intervalUnit.toMillis(intervalCount));
        scheduledProcess.setRepeatCountsLeft(repeatCount);
        this.storeNewScheduledProcess(scheduledProcess);
    }
    
    public void schedule(final Runnable runnable, final long delay, final int repeatCount) {
        if (runnable == null) {
            ProcessScheduler.m_logger.error((Object)"Tentative d'insertion d'un Runnable null");
            return;
        }
        if (repeatCount == 0) {
            ProcessScheduler.m_logger.warn((Object)"On schedule un runnable pour \u00eatre execut\u00e9 0 fois, WTF ? (Gros blaireau)");
            return;
        }
        final ScheduledProcess scheduledProcess = new ScheduledProcessOnFixedInterval(runnable, delay);
        scheduledProcess.setRepeatCountsLeft(repeatCount);
        this.storeNewScheduledProcess(scheduledProcess);
    }
    
    public void scheduleAndInvokeNow(final Runnable runnable, final long delay) {
        this.schedule(runnable);
        this.schedule(runnable, delay);
    }
    
    private void storeNewScheduledProcess(final ScheduledProcess scheduledProcess) {
        final SchedulerOperation op = new SchedulerOperation(SchedulerOperationType.SCHEDULE, scheduledProcess, null);
        this.m_operations.offer(op);
        this.m_numPendingOperations.incrementAndGet();
        if (Worker.getInstance().isRunning()) {
            Worker.getInstance().wakeUp();
        }
        else {
            ProcessScheduler.m_logger.error((Object)"Worker is not running, unable to wakeUp!");
        }
    }
    
    public void remove(final Runnable runnable) {
        final SchedulerOperation op = new SchedulerOperation(SchedulerOperationType.REMOVE, null, runnable);
        this.m_operations.offer(op);
        this.m_numPendingOperations.incrementAndGet();
    }
    
    public void cancelAllSchedules() {
        this.m_processes.clear();
        this.m_operations.clear();
        this.m_numPendingOperations.set(0);
        this.m_numPendingSchedules.set(0);
    }
    
    private void reschedule(final ScheduledProcess process, final long referenceTime) {
        final long nextSchedulingTime = process.computeNextSchedulingTime(referenceTime);
        for (int i = 0; i < this.m_processes.size(); ++i) {
            final ScheduledProcess scheduledProcess = this.m_processes.get(i);
            if (nextSchedulingTime < scheduledProcess.getNextSchedulingTime()) {
                this.m_processes.add(i, process);
                return;
            }
        }
        this.m_processes.add(process);
    }
    
    public long getMaximalSleepTime() {
        if (this.m_processes.isEmpty()) {
            return 30L;
        }
        final long maxSleepTime = this.m_processes.get(0).getNextSchedulingTime() - System.currentTimeMillis();
        return Math.max(0L, maxSleepTime);
    }
    
    public void prepareForUpdate() {
        if (this.m_processes.isEmpty() && this.m_operations.peek() == null) {
            return;
        }
        this.performPendingOperations();
    }
    
    private void performPendingOperations() {
        final long now = System.currentTimeMillis();
        SchedulerOperation op;
        while ((op = this.m_operations.poll()) != null) {
            switch (op.getOp()) {
                case REMOVE: {
                    final Iterator<ScheduledProcess> it = this.m_processes.iterator();
                    while (it.hasNext()) {
                        final ScheduledProcess process = it.next();
                        if (process.getRunnable() == op.getRunnable()) {
                            this.m_numPendingSchedules.decrementAndGet();
                            it.remove();
                            break;
                        }
                    }
                    break;
                }
                case SCHEDULE: {
                    this.m_numPendingSchedules.incrementAndGet();
                    this.reschedule(op.getProcess(), now);
                    break;
                }
                default: {
                    ProcessScheduler.m_logger.error((Object)("Undefined operation ! : " + op.getOp()));
                    break;
                }
            }
            this.m_numPendingOperations.decrementAndGet();
        }
    }
    
    public void update() {
        if (this.m_processes.isEmpty() && this.m_operations.peek() == null) {
            return;
        }
        long referenceTime = System.currentTimeMillis();
        this.performPendingOperations();
        final Iterator<ScheduledProcess> it = this.m_processes.iterator();
        while (it.hasNext()) {
            final ScheduledProcess scheduledProcess = it.next();
            final long deltaTime = referenceTime - scheduledProcess.getNextSchedulingTime();
            if (deltaTime >= 0L) {
                it.remove();
                this.m_numPendingSchedules.decrementAndGet();
                int countsLeft = scheduledProcess.getRepeatCountsLeft();
                if (countsLeft == 0) {
                    continue;
                }
                if (countsLeft > 0) {
                    --countsLeft;
                }
                scheduledProcess.setRepeatCountsLeft(countsLeft);
                Runnable process = null;
                try {
                    process = scheduledProcess.getRunnable();
                    if (process != null) {
                        process.run();
                    }
                    else {
                        ProcessScheduler.m_logger.error((Object)"(Paranoia) Process null ?!");
                    }
                }
                catch (Throwable e) {
                    ProcessScheduler.m_logger.error((Object)((process != null) ? ("ProcessScheduler exception (" + process.getClass().getName() + "): ") : "ProcessScheduler exception (null process): "), e);
                }
                if (countsLeft == 0) {
                    continue;
                }
                this.m_immediateReschedule.add(scheduledProcess);
            }
        }
        if (!this.m_immediateReschedule.isEmpty()) {
            referenceTime = System.currentTimeMillis();
            for (int i = 0, size = this.m_immediateReschedule.size(); i < size; ++i) {
                this.reschedule(this.m_immediateReschedule.get(i), referenceTime);
            }
            this.m_immediateReschedule.clear();
        }
        this.performPendingOperations();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ProcessScheduler.class);
        m_instance = new ProcessScheduler();
    }
}
