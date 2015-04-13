package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;

public final class MonsterActionGroupManager
{
    public static final MonsterActionGroupManager INSTANCE;
    private final QueueActionGroupManager m_manager;
    
    private MonsterActionGroupManager() {
        super();
        this.m_manager = new QueueActionGroupManager();
    }
    
    public ActionGroup addAction(final Action action) {
        if (action == null) {
            return null;
        }
        return this.m_manager.addActionToExecutingGroup(action);
    }
    
    public void executeAllAction() {
        this.m_manager.executeAllAction();
    }
    
    public void clear() {
        this.m_manager.clear();
    }
    
    static {
        INSTANCE = new MonsterActionGroupManager();
    }
}
