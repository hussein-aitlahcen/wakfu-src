package com.ankamagames.framework.script.action;

import org.apache.log4j.*;
import java.util.*;

public abstract class Action
{
    protected static Logger m_logger;
    public static final long NO_TARGET_ID = Long.MIN_VALUE;
    public static final int NO_TRIGGER_ACTION_ID = -1;
    protected ArrayList<ActionEventListener> m_listeners;
    private int m_uniqueId;
    private int m_actionType;
    private int m_actionId;
    private long m_instigatorId;
    private long m_targetId;
    private boolean m_runned;
    private int m_triggerActionUniqueId;
    private ActionGroup m_group;
    
    public Action(final int uniqueId, final int actionType, final int actionId) {
        super();
        this.m_listeners = new ArrayList<ActionEventListener>();
        this.m_instigatorId = Long.MIN_VALUE;
        this.m_targetId = Long.MIN_VALUE;
        this.m_runned = false;
        this.m_triggerActionUniqueId = -1;
        this.m_uniqueId = uniqueId;
        this.m_actionType = actionType;
        this.m_actionId = actionId;
    }
    
    public abstract void run();
    
    public void addListener(final ActionEventListener listener) {
        this.m_listeners.add(listener);
    }
    
    public void removeListener(final ActionEventListener listener) {
        this.m_listeners.remove(listener);
    }
    
    public int getUniqueId() {
        return this.m_uniqueId;
    }
    
    public void setUniqueId(final int uniqueId) {
        this.m_uniqueId = uniqueId;
    }
    
    public int getActionType() {
        return this.m_actionType;
    }
    
    public void setActionType(final int actionType) {
        this.m_actionType = actionType;
    }
    
    public int getActionId() {
        return this.m_actionId;
    }
    
    public void setActionId(final int actionId) {
        this.m_actionId = actionId;
    }
    
    public long getInstigatorId() {
        return this.m_instigatorId;
    }
    
    public void setInstigatorId(final long instigatorId) {
        this.m_instigatorId = instigatorId;
    }
    
    public int getTriggerActionUniqueId() {
        return this.m_triggerActionUniqueId;
    }
    
    public void setTriggerActionUniqueId(final int triggerActionUniqueId) {
        this.m_triggerActionUniqueId = triggerActionUniqueId;
    }
    
    public long getTargetId() {
        return this.m_targetId;
    }
    
    public void setTargetId(final long targetId) {
        this.m_targetId = targetId;
    }
    
    public void setGroup(final ActionGroup group) {
        this.m_group = group;
    }
    
    public ActionGroup getGroup() {
        return this.m_group;
    }
    
    protected void fireActionFinishedEvent() {
        try {
            this.onActionFinished();
        }
        catch (Exception e) {
            Action.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        for (final ActionEventListener listener : this.m_listeners.toArray(new ActionEventListener[this.m_listeners.size()])) {
            try {
                listener.onActionFinished(this);
            }
            catch (Exception e2) {
                Action.m_logger.error((Object)"Exception levee", (Throwable)e2);
            }
        }
    }
    
    public boolean hasRunned() {
        return this.m_runned;
    }
    
    public void setHasRunned(final boolean runned) {
        this.m_runned = runned;
    }
    
    protected abstract void onActionFinished();
    
    @Override
    public String toString() {
        return "{Action UID=" + this.getUniqueId() + " id=" + this.getActionId() + " type=" + this.getActionType() + "}";
    }
    
    public int getSpecialId() {
        return -1;
    }
    
    static {
        Action.m_logger = Logger.getLogger((Class)Action.class);
    }
}
