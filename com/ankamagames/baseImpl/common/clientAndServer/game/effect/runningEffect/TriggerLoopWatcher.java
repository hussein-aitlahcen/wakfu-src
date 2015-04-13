package com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect;

import java.util.*;

public final class TriggerLoopWatcher
{
    public static final TriggerLoopWatcher INSTANCE;
    private TriggerLoopWatcherNode m_current;
    private final Collection<TriggerInfo> m_executedTriggers;
    
    TriggerLoopWatcher() {
        super();
        this.m_executedTriggers = new HashSet<TriggerInfo>();
    }
    
    public TriggerLoopWatcherNode newTrigger(final int effectId, final long casterId, final long targetId) {
        final TriggerLoopWatcherNode node = new TriggerLoopWatcherNode(effectId, casterId, targetId);
        if (this.m_current != null) {
            this.m_current.addChild(node);
        }
        this.m_current = node;
        this.m_executedTriggers.add(new TriggerInfo(effectId, casterId, targetId));
        return node;
    }
    
    void endCurrentTrigger() {
        final TriggerLoopWatcherNode previousCurrent = this.m_current;
        if (this.m_current.getParent() != null) {
            previousCurrent.getParent().removeChild(this.m_current);
        }
        this.m_current = this.m_current.getParent();
        previousCurrent.clear();
    }
    
    public boolean currentIsOrHasParent(final int effectId, final long targetId) {
        return this.m_current != null && ((this.m_current.getEffectId() == effectId && this.m_current.getTargetId() == targetId) || this.m_current.hasParent(effectId, targetId));
    }
    
    TriggerLoopWatcherNode getCurrent() {
        return this.m_current;
    }
    
    public void clear() {
        this.m_current = null;
        this.m_executedTriggers.clear();
    }
    
    @Override
    public String toString() {
        if (this.m_current == null) {
            return "empty";
        }
        return this.m_current.toString();
    }
    
    public boolean hasBeenTriggered(final int effectId, final long casterId, final long targetId) {
        return this.m_executedTriggers.contains(new TriggerInfo(effectId, casterId, targetId));
    }
    
    static {
        INSTANCE = new TriggerLoopWatcher();
    }
    
    private final class TriggerInfo
    {
        private final int m_effectId;
        private final long m_casterId;
        private final long m_targetId;
        
        private TriggerInfo(final int effectId, final long casterId, final long targetId) {
            super();
            this.m_effectId = effectId;
            this.m_casterId = casterId;
            this.m_targetId = targetId;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TriggerInfo)) {
                return false;
            }
            final TriggerInfo that = (TriggerInfo)o;
            return this.m_casterId == that.m_casterId && this.m_effectId == that.m_effectId && this.m_targetId == that.m_targetId;
        }
        
        @Override
        public int hashCode() {
            int result = this.m_effectId;
            result = 31 * result + (int)(this.m_casterId ^ this.m_casterId >>> 32);
            result = 31 * result + (int)(this.m_targetId ^ this.m_targetId >>> 32);
            return result;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("IntLongLong");
            sb.append("{m_effectId=").append(this.m_effectId);
            sb.append(", m_casterId=").append(this.m_casterId);
            sb.append(", m_targetId=").append(this.m_targetId);
            sb.append('}');
            return sb.toString();
        }
    }
}
