package com.ankamagames.wakfu.client.core.action;

import org.apache.log4j.*;
import com.ankamagames.framework.script.action.*;

public class WorldActionGroupManager
{
    protected static Logger m_logger;
    private static WorldActionGroupManager m_instance;
    private QueueActionGroupManager m_manager;
    
    public WorldActionGroupManager() {
        super();
        this.m_manager = new QueueActionGroupManager();
    }
    
    public static WorldActionGroupManager getInstance() {
        return WorldActionGroupManager.m_instance;
    }
    
    public ActionGroup addAction(final Action action) {
        if (action == null) {
            return null;
        }
        return this.m_manager.addActionToPendingGroup(action);
    }
    
    public void executePendingGroup() {
        this.m_manager.executePendingGroup();
    }
    
    public void removePendingGroups() {
        this.m_manager.killActionsToPendingGroup();
    }
    
    public QueueActionGroupManager getActionManager() {
        return this.m_manager;
    }
    
    public void clear() {
        this.m_manager.clear();
    }
    
    static {
        WorldActionGroupManager.m_logger = Logger.getLogger((Class)FightActionGroupManager.class);
        WorldActionGroupManager.m_instance = new WorldActionGroupManager();
    }
}
