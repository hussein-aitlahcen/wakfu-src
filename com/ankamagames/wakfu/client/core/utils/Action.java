package com.ankamagames.wakfu.client.core.utils;

public class Action
{
    private final String m_target;
    private final int m_actionType;
    
    public Action(final String target, final int actionType) {
        super();
        this.m_target = target;
        this.m_actionType = actionType;
    }
    
    public String getTarget() {
        return this.m_target;
    }
    
    public int getActionType() {
        return this.m_actionType;
    }
    
    public boolean equals(final Action action) {
        return this.m_target != null && this.m_actionType == action.getActionType() && this.m_target.equals(action.getTarget());
    }
}
