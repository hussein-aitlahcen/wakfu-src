package com.ankamagames.framework.kernel.monitoring;

import org.apache.log4j.*;
import java.util.concurrent.locks.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class ClockMessageStats
{
    private String messageHandlerClassName;
    private final int[] messageExecutionTime;
    private static final THashMap<String, ClockMessageStats> CLOCK_MESSAGE_STATS;
    private static final Logger m_logger;
    private static final ReentrantReadWriteLock m_rwLock;
    private static final Lock m_rLock;
    private static final Lock m_wLock;
    
    public ClockMessageStats(final String className) {
        super();
        this.messageExecutionTime = new int[5];
        this.reset();
        this.messageHandlerClassName = className;
    }
    
    public void updateExecutionTime(final int duration, final boolean succeeded) {
        if (succeeded) {
            final int[] messageExecutionTime = this.messageExecutionTime;
            final int n = 3;
            ++messageExecutionTime[n];
        }
        else {
            final int[] messageExecutionTime2 = this.messageExecutionTime;
            final int n2 = 4;
            ++messageExecutionTime2[n2];
        }
        if (duration < this.messageExecutionTime[0]) {
            this.messageExecutionTime[0] = duration;
        }
        if (duration > this.messageExecutionTime[1]) {
            this.messageExecutionTime[1] = duration;
        }
        this.messageExecutionTime[2] += duration;
    }
    
    public void reset() {
        Arrays.fill(this.messageExecutionTime, 0);
        this.messageExecutionTime[0] = Integer.MAX_VALUE;
    }
    
    public String getMessageHandlerClassName() {
        return this.messageHandlerClassName;
    }
    
    public int getMessageExecutionTime(final int index) {
        return this.messageExecutionTime[index];
    }
    
    public static ArrayList<String> handlerNames() {
        final ArrayList<String> names = new ArrayList<String>();
        ClockMessageStats.m_rLock.lock();
        try {
            if (!ClockMessageStats.CLOCK_MESSAGE_STATS.isEmpty()) {
                ClockMessageStats.CLOCK_MESSAGE_STATS.forEachKey(new TObjectProcedure<String>() {
                    @Override
                    public boolean execute(final String a) {
                        names.add(a);
                        return true;
                    }
                });
            }
        }
        catch (Exception e) {
            ClockMessageStats.m_logger.error((Object)"Exception", (Throwable)e);
        }
        finally {
            ClockMessageStats.m_rLock.unlock();
        }
        return names;
    }
    
    public static ClockMessageStats getStatsOf(final ClockMessage clockMessage, final boolean createIfNeeded) {
        final MessageHandler handler = clockMessage.getHandler();
        String handlerName;
        if (handler == null) {
            handlerName = "null";
        }
        else {
            handlerName = handler.getClass().getSimpleName();
        }
        return getStatsOf(handlerName, createIfNeeded);
    }
    
    public static ClockMessageStats getStatsOf(final String handlerName, final boolean createIfNeeded) {
        ClockMessageStats.m_rLock.lock();
        ClockMessageStats stats = null;
        try {
            stats = ClockMessageStats.CLOCK_MESSAGE_STATS.get(handlerName);
        }
        catch (Exception e) {
            ClockMessageStats.m_logger.error((Object)"Exception", (Throwable)e);
        }
        finally {
            ClockMessageStats.m_rLock.unlock();
        }
        if (stats == null && createIfNeeded) {
            stats = new ClockMessageStats(handlerName);
            ClockMessageStats.m_wLock.lock();
            try {
                ClockMessageStats.CLOCK_MESSAGE_STATS.put(handlerName, stats);
            }
            catch (Exception e) {
                ClockMessageStats.m_logger.error((Object)"Exception", (Throwable)e);
            }
            finally {
                ClockMessageStats.m_wLock.unlock();
            }
        }
        return stats;
    }
    
    static {
        CLOCK_MESSAGE_STATS = new THashMap<String, ClockMessageStats>();
        m_logger = Logger.getLogger((Class)ClockMessageStats.class);
        m_rwLock = new ReentrantReadWriteLock();
        m_rLock = ClockMessageStats.m_rwLock.readLock();
        m_wLock = ClockMessageStats.m_rwLock.writeLock();
    }
}
