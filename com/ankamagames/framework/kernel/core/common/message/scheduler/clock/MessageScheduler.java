package com.ankamagames.framework.kernel.core.common.message.scheduler.clock;

import org.apache.log4j.*;
import gnu.trove.*;
import java.util.concurrent.locks.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import java.util.*;
import java.util.concurrent.*;

public class MessageScheduler extends Thread
{
    private static final long ROUNDING_TRIP = 3L;
    private static final Logger m_logger;
    protected static MessageScheduler m_instance;
    protected boolean m_running;
    protected final TLinkedList<SchedulerListener> m_primaryListeners;
    protected final Queue<ClockOperation> m_clockOperations;
    protected final Lock m_wakeUpMutex;
    protected final Condition m_wakeUpCond;
    protected long m_lastClockId;
    
    protected MessageScheduler() {
        super("MessageScheduler");
        this.m_primaryListeners = new TLinkedList<SchedulerListener>();
        this.m_clockOperations = new ConcurrentLinkedQueue<ClockOperation>();
        this.m_wakeUpMutex = new ReentrantLock();
        this.m_wakeUpCond = this.m_wakeUpMutex.newCondition();
        this.m_running = false;
    }
    
    public static MessageScheduler getInstance() {
        return MessageScheduler.m_instance;
    }
    
    private void wakeUp() {
        this.m_wakeUpMutex.lock();
        this.m_wakeUpCond.signalAll();
        this.m_wakeUpMutex.unlock();
    }
    
    public long addClock(final MessageHandler messageHandler, final long clockDelay, final int clockSubId) {
        return this.addClock(messageHandler, clockDelay, clockSubId, -1);
    }
    
    public long addClock(final MessageHandler messageHandler, final long clockDelay, final int clockSubId, final int executionsCount) {
        final SchedulerListener listener = new SchedulerListener();
        listener.setItem(messageHandler);
        listener.setClockDelay(clockDelay);
        listener.setSubId(clockSubId);
        listener.setRepetitionsCount(executionsCount);
        listener.setTriggered(System.currentTimeMillis());
        listener.setClockId(++this.m_lastClockId);
        final ClockOperation op = new ClockOperation();
        op.op = ClockOp.ADD_CLOCK_LISTENER;
        op.listener = listener;
        this.m_clockOperations.offer(op);
        this.wakeUp();
        return listener.getClockId();
    }
    
    public void removeClock(final long clockId) {
        final ClockOperation op = new ClockOperation();
        op.op = ClockOp.REMOVE_CLOCK_BY_ID;
        op.clockId = clockId;
        this.m_clockOperations.offer(op);
        this.wakeUp();
    }
    
    public void removeAllClocks() {
        final ClockOperation op = new ClockOperation();
        op.op = ClockOp.REMOVE_ALL_CLOCKS;
        this.m_clockOperations.offer(op);
        this.wakeUp();
    }
    
    public void removeAllClocks(final MessageHandler messageHandler) {
        final ClockOperation op = new ClockOperation();
        op.op = ClockOp.REMOVE_CLOCKS_BY_HANDLER;
        op.handler = messageHandler;
        this.m_clockOperations.offer(op);
        this.wakeUp();
    }
    
    public void removeAllClocks(final MessageHandler messageHandler, final int clockSubId) {
        final ClockOperation op = new ClockOperation();
        op.op = ClockOp.REMOVE_CLOCKS_BY_HANDLER_AND_SUBID;
        op.handler = messageHandler;
        op.subClockId = clockSubId;
        this.m_clockOperations.offer(op);
        this.wakeUp();
    }
    
    @Override
    public void start() {
        if (!this.m_running) {
            this.m_running = true;
            super.start();
        }
    }
    
    public boolean isRunning() {
        return this.m_running;
    }
    
    public void setRunning(final boolean running) {
        this.m_running = running;
        this.wakeUp();
    }
    
    private void insertListener(final SchedulerListener listener) {
        boolean bInserted = false;
        final long listenerNextTime = listener.getNextTime();
        for (final SchedulerListener nListener : this.m_primaryListeners) {
            if (nListener.getNextTime() > listenerNextTime) {
                this.m_primaryListeners.addBefore(nListener, listener);
                bInserted = true;
                break;
            }
        }
        if (!bInserted) {
            this.m_primaryListeners.add(listener);
        }
    }
    
    private void pushClockMessageForListener(final SchedulerListener listener, final long referenceTime) {
        try {
            final ClockMessage message = new ClockMessage();
            message.setHandler(listener.getItem());
            message.setClockId(listener.getClockId());
            message.setSubId(listener.getSubId());
            message.setTimeStamp(referenceTime);
            Worker.getInstance().pushMessage(message);
        }
        catch (Exception e) {
            MessageScheduler.m_logger.error((Object)("Unable to push ClockMessage, exception raised : " + e.getMessage()));
        }
    }
    
    private void updateListeners() {
        while (!this.m_clockOperations.isEmpty()) {
            final ClockOperation op = this.m_clockOperations.poll();
            switch (op.op) {
                case ADD_CLOCK_LISTENER: {
                    this.insertListener(op.listener);
                    continue;
                }
                case REMOVE_CLOCK_BY_ID: {
                    final Iterator<SchedulerListener> it = this.m_primaryListeners.iterator();
                    while (it.hasNext()) {
                        final SchedulerListener listener = it.next();
                        if (listener.getClockId() == op.clockId) {
                            listener.discard();
                            it.remove();
                            break;
                        }
                    }
                    continue;
                }
                case REMOVE_CLOCKS_BY_HANDLER: {
                    final Iterator<SchedulerListener> it = this.m_primaryListeners.iterator();
                    while (it.hasNext()) {
                        final SchedulerListener listener = it.next();
                        if (listener.getItem() == op.handler) {
                            it.remove();
                        }
                    }
                    continue;
                }
                case REMOVE_CLOCKS_BY_HANDLER_AND_SUBID: {
                    final Iterator<SchedulerListener> it = this.m_primaryListeners.iterator();
                    while (it.hasNext()) {
                        final SchedulerListener listener = it.next();
                        if (listener.getItem() == op.handler && listener.getSubId() == op.subClockId) {
                            it.remove();
                        }
                    }
                    continue;
                }
                case REMOVE_ALL_CLOCKS: {
                    this.m_primaryListeners.clear();
                    continue;
                }
            }
        }
    }
    
    @Override
    public void run() {
        final ArrayList<SchedulerListener> m_listenersToReschedule = new ArrayList<SchedulerListener>();
        MessageScheduler.m_logger.info((Object)"MessageScheduler running");
        while (this.m_running) {
            try {
                try {
                    if (!this.m_primaryListeners.isEmpty()) {
                        final long referenceTime = System.currentTimeMillis();
                        m_listenersToReschedule.clear();
                        final Iterator<SchedulerListener> it = this.m_primaryListeners.iterator();
                        while (it.hasNext()) {
                            final SchedulerListener listener = it.next();
                            if (listener.getNextTime() <= referenceTime + 3L) {
                                if (!listener.isDiscarded()) {
                                    this.pushClockMessageForListener(listener, referenceTime);
                                    listener.setTriggered(referenceTime);
                                    it.remove();
                                    if (!listener.canBeRepeated()) {
                                        continue;
                                    }
                                    m_listenersToReschedule.add(listener);
                                }
                                else {
                                    it.remove();
                                }
                            }
                            else {
                                if (m_listenersToReschedule.isEmpty()) {
                                    this.m_wakeUpMutex.lock();
                                    this.m_wakeUpCond.await(Math.max(1L, listener.getNextTime() - referenceTime), TimeUnit.MILLISECONDS);
                                    this.m_wakeUpCond.signalAll();
                                    this.m_wakeUpMutex.unlock();
                                    break;
                                }
                                break;
                            }
                        }
                        if (!m_listenersToReschedule.isEmpty()) {
                            for (final SchedulerListener schedulerListener : m_listenersToReschedule) {
                                this.insertListener(schedulerListener);
                            }
                        }
                        this.updateListeners();
                    }
                    else {
                        if (this.m_wakeUpMutex.tryLock()) {
                            this.m_wakeUpCond.await();
                            this.m_wakeUpMutex.unlock();
                        }
                        this.updateListeners();
                    }
                }
                catch (Exception e) {
                    MessageScheduler.m_logger.error((Object)"Exception lev\u00e9e : ", (Throwable)e);
                }
            }
            catch (Exception ex) {
                MessageScheduler.m_logger.error((Object)"Exception", (Throwable)ex);
            }
        }
        MessageScheduler.m_logger.info((Object)"Message Scheduler stopped");
    }
    
    public final String getClocksSummary() {
        final long now = System.currentTimeMillis();
        final Iterator<SchedulerListener> it = this.m_primaryListeners.iterator();
        final StringBuilder result = new StringBuilder();
        result.append("Found ").append(this.m_primaryListeners.size()).append(" clocks:\n");
        while (it.hasNext()) {
            final SchedulerListener listener = it.next();
            result.append(listener.getItem().getClass().getSimpleName()).append(" : ").append(listener.getClockDelay()).append(" ms ");
            if (listener.canBeRepeated()) {
                result.append("repeatable ").append(listener.getRepetitionsCount()).append(" times ");
            }
            result.append("next tick in ").append(listener.getNextTime() - now).append(" ms\n");
        }
        return result.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)MessageScheduler.class);
        MessageScheduler.m_instance = new MessageScheduler();
    }
    
    private enum ClockOp
    {
        REMOVE_CLOCK_BY_ID, 
        REMOVE_CLOCKS_BY_HANDLER, 
        REMOVE_CLOCKS_BY_HANDLER_AND_SUBID, 
        REMOVE_ALL_CLOCKS, 
        ADD_CLOCK_LISTENER;
    }
    
    private static class ClockOperation
    {
        ClockOp op;
        SchedulerListener listener;
        long clockId;
        MessageHandler handler;
        int subClockId;
    }
}
