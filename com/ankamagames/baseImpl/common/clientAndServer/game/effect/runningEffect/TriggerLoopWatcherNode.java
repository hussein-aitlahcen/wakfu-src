package com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect;

import java.util.*;

public final class TriggerLoopWatcherNode
{
    private final int m_effectId;
    private final long m_casterId;
    private final long m_targetId;
    private final Collection<TriggerLoopWatcherNode> m_children;
    private TriggerLoopWatcherNode m_parent;
    
    TriggerLoopWatcherNode(final int effectId, final long casterId, final long targetId) {
        super();
        this.m_children = new ArrayList<TriggerLoopWatcherNode>();
        this.m_effectId = effectId;
        this.m_casterId = casterId;
        this.m_targetId = targetId;
    }
    
    void addChild(final TriggerLoopWatcherNode node) {
        this.m_children.add(node);
        node.m_parent = this;
    }
    
    boolean removeChild(final TriggerLoopWatcherNode node) {
        return this.m_children.remove(node);
    }
    
    boolean hasParent(final int effectId, final long targetId) {
        return this.m_parent != null && ((this.m_parent.m_effectId == effectId && this.m_parent.m_targetId == targetId) || this.m_parent.hasParent(effectId, targetId));
    }
    
    boolean hasParent(final int effectId, final long targetId, final long casterId) {
        return this.m_parent != null && ((this.m_parent.m_effectId == effectId && this.m_parent.m_targetId == targetId && this.m_parent.m_casterId == casterId) || this.m_parent.hasParent(effectId, targetId, casterId));
    }
    
    public boolean hasChild(final TriggerLoopWatcherNode node) {
        return this.m_children.contains(node);
    }
    
    public int getEffectId() {
        return this.m_effectId;
    }
    
    public TriggerLoopWatcherNode getParent() {
        return this.m_parent;
    }
    
    public long getTargetId() {
        return this.m_targetId;
    }
    
    public void clear() {
        this.m_parent = null;
        this.m_children.clear();
    }
    
    @Override
    public String toString() {
        if (this.m_parent == null) {
            return this.m_effectId + "( casterId = " + this.m_casterId + "), ( targetId = " + this.m_targetId + ")";
        }
        return this.m_effectId + "( casterId = " + this.m_casterId + "), ( targetId = " + this.m_targetId + ") <- " + this.m_parent.toString();
    }
}
