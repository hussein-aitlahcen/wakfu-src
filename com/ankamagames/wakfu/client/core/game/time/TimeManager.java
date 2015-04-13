package com.ankamagames.wakfu.client.core.game.time;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;

public class TimeManager implements MessageHandler
{
    private static final Logger m_logger;
    public static final TimeManager INSTANCE;
    private final ArrayList<TimeTickListener> m_listeners;
    
    public TimeManager() {
        super();
        this.m_listeners = new ArrayList<TimeTickListener>();
    }
    
    public void addListener(final TimeTickListener l) {
        synchronized (this.m_listeners) {
            if (!this.m_listeners.contains(l)) {
                this.m_listeners.add(l);
            }
        }
    }
    
    public void removeListener(final TimeTickListener l) {
        synchronized (this.m_listeners) {
            this.m_listeners.remove(l);
        }
    }
    
    public void start() {
        MessageScheduler.getInstance().addClock(this, 1000L, -1);
    }
    
    public void stop() {
        MessageScheduler.getInstance().removeAllClocks(this);
        synchronized (this.m_listeners) {
            this.m_listeners.clear();
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (message instanceof ClockMessage) {
            synchronized (this.m_listeners) {
                for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
                    try {
                        this.m_listeners.get(i).tick();
                    }
                    catch (Throwable t) {
                        TimeManager.m_logger.warn((Object)"Erreur durant un TimeTick", t);
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public long getId() {
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TimeManager");
        sb.append("{m_listeners=").append(this.m_listeners);
        sb.append('}');
        return sb.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)TimeManager.class);
        INSTANCE = new TimeManager();
    }
}
