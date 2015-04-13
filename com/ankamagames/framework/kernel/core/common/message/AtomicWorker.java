package com.ankamagames.framework.kernel.core.common.message;

import org.apache.log4j.*;
import java.util.concurrent.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.monitoring.*;
import gnu.trove.*;
import java.util.*;

class AtomicWorker implements Runnable
{
    private static final Logger m_logger;
    private final ConcurrentLinkedQueue<Message> m_messages;
    
    AtomicWorker() {
        super();
        this.m_messages = new ConcurrentLinkedQueue<Message>();
    }
    
    @Override
    public void run() {
        this.runPrimaryQueue();
        ProcessScheduler.getInstance().prepareForUpdate();
        ProcessScheduler.getInstance().update();
    }
    
    private void runPrimaryQueue() {
        while (!this.m_messages.isEmpty()) {
            final Message message = this.m_messages.poll();
            if (message == null) {
                continue;
            }
            try {
                executeMessage(message);
            }
            catch (Throwable e) {
                AtomicWorker.m_logger.error((Object)("Error during execution of message " + message), e);
            }
        }
    }
    
    private static void executeMessage(final Message message) {
        final String messageClassName = message.getClass().getSimpleName();
        final MessageHandler handler = message.getHandler();
        if (handler == null) {
            AtomicWorker.m_logger.error((Object)("Destinataire invalide pour un message de type " + messageClassName + ", destinataire : " + "null"));
            return;
        }
        final long before = System.nanoTime();
        try {
            message.execute();
        }
        catch (RuntimeException e) {
            AtomicWorker.m_logger.error((Object)("Exception lev\u00e9e lors de l'\u00e9x\u00e9cution d'un message (id=" + message.getId() + "): "), (Throwable)e);
        }
        final int duration = (int)((System.nanoTime() - before) / 1000000L);
        if (message instanceof ClockMessage) {
            final ClockMessageStats clockStats = ClockMessageStats.getStatsOf((ClockMessage)message, true);
            clockStats.updateExecutionTime(duration, true);
        }
    }
    
    boolean offer(final Message message) {
        return this.m_messages.offer(message);
    }
    
    int messageSize() {
        return this.m_messages.size();
    }
    
    void clear() {
        this.m_messages.clear();
    }
    
    public String getQueueSummary() {
        final TObjectIntHashMap<String> queueMessages = new TObjectIntHashMap<String>();
        final TObjectLongHashMap<String> minTimestamps = new TObjectLongHashMap<String>();
        final TObjectLongHashMap<String> maxTimestamps = new TObjectLongHashMap<String>();
        final TObjectLongHashMap<String> totalTimestamps = new TObjectLongHashMap<String>();
        final Iterator<Message> it = this.m_messages.iterator();
        final long now = System.currentTimeMillis();
        while (it.hasNext()) {
            final Message message = it.next();
            final String keyName = message.getClass().getSimpleName();
            final long timeStamp = now - message.getWorkerTimeStamp();
            queueMessages.adjustOrPutValue(keyName, 1, 1);
            final long lastMin = minTimestamps.contains(keyName) ? minTimestamps.get(keyName) : timeStamp;
            final long lastMax = maxTimestamps.contains(keyName) ? maxTimestamps.get(keyName) : timeStamp;
            minTimestamps.put(keyName, Math.min(lastMin, timeStamp));
            maxTimestamps.put(keyName, Math.max(lastMax, timeStamp));
            totalTimestamps.adjustOrPutValue(keyName, timeStamp, timeStamp);
        }
        if (!queueMessages.isEmpty()) {
            final StringBuilder result = new StringBuilder();
            if (!queueMessages.isEmpty()) {
                queueMessages.forEachEntry(new TObjectIntProcedure<String>() {
                    @Override
                    public boolean execute(final String message, final int count) {
                        result.append(count).append(" x ").append(message).append(" - ").append(minTimestamps.get(message)).append(" / ").append(totalTimestamps.get(message) / count).append(" / ").append(maxTimestamps.get(message)).append("\n");
                        return true;
                    }
                });
            }
            return result.toString();
        }
        return "(empty)";
    }
    
    @Override
    public String toString() {
        return "AtomicWorker{m_messages=" + this.m_messages.size() + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)AtomicWorker.class);
    }
}
