package com.ankamagames.xulor2.core.event;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class Event implements Poolable, Releasable
{
    private static final Logger m_logger;
    private static final ObjectPool m_pool;
    protected ObjectPool m_currentPool;
    protected boolean m_runAfterOnTarget;
    protected Events m_type;
    protected boolean m_isSoundConsumed;
    protected EventDispatcher m_target;
    protected EventDispatcher m_currentTarget;
    protected final ArrayList<EventDispatcher> m_pathFromRoot;
    
    public Event() {
        super();
        this.m_runAfterOnTarget = false;
        this.m_type = null;
        this.m_isSoundConsumed = false;
        this.m_pathFromRoot = new ArrayList<EventDispatcher>();
    }
    
    public void pushEventDispatcher(final EventDispatcher dispatcher) {
        this.m_pathFromRoot.add(dispatcher);
    }
    
    public EventDispatcher popNextEventDispatcher() {
        final int size = this.m_pathFromRoot.size();
        return (size > 0) ? this.m_pathFromRoot.remove(size - 1) : null;
    }
    
    public boolean isGoingUp() {
        return !this.m_pathFromRoot.isEmpty();
    }
    
    public boolean isRunAfterOnTarget() {
        return this.m_runAfterOnTarget;
    }
    
    public void setRunAfterOnTarget(final boolean bubbles) {
        this.m_runAfterOnTarget = bubbles;
    }
    
    public <T extends EventDispatcher> T getCurrentTarget() {
        return (T)this.m_currentTarget;
    }
    
    public void setCurrentTarget(final EventDispatcher currentTarget) {
        this.m_currentTarget = currentTarget;
    }
    
    public <T extends EventDispatcher> T getTarget() {
        return (T)this.m_target;
    }
    
    public void setTarget(final EventDispatcher target) {
        this.m_target = target;
    }
    
    public Events getType() {
        return this.m_type;
    }
    
    public void setType(final Events type) {
        this.m_type = type;
    }
    
    public boolean isSoundConsumed() {
        return this.m_isSoundConsumed;
    }
    
    public void setSoundConsumed(final boolean soundConsumed) {
        this.m_isSoundConsumed = soundConsumed;
    }
    
    public static Event checkOut() {
        Event e;
        try {
            e = (Event)Event.m_pool.borrowObject();
            e.m_currentPool = Event.m_pool;
        }
        catch (Exception ex) {
            Event.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            e = new Event();
            e.onCheckOut();
        }
        return e;
    }
    
    @Override
    public void release() {
        if (this.m_currentPool == null) {
            this.onCheckIn();
            return;
        }
        try {
            this.m_currentPool.returnObject(this);
        }
        catch (Exception e) {
            this.onCheckIn();
        }
    }
    
    @Override
    public void onCheckIn() {
        this.m_target = null;
        this.m_currentTarget = null;
        this.m_pathFromRoot.clear();
        this.m_currentPool = null;
    }
    
    @Override
    public void onCheckOut() {
        this.m_isSoundConsumed = false;
        this.m_runAfterOnTarget = false;
    }
    
    @Override
    public String toString() {
        return "Event type=" + this.getType();
    }
    
    static {
        m_logger = Logger.getLogger((Class)Event.class);
        m_pool = new MonitoredPool(new ObjectFactory<Event>() {
            @Override
            public Event makeObject() {
                return new Event();
            }
        });
    }
}
