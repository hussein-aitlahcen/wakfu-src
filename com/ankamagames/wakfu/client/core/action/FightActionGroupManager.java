package com.ankamagames.wakfu.client.core.action;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

public class FightActionGroupManager
{
    protected static final Logger m_logger;
    private static final FightActionGroupManager m_instance;
    private final TIntObjectHashMap<QueueActionGroupManager> m_managers;
    
    private FightActionGroupManager() {
        super();
        this.m_managers = new TIntObjectHashMap<QueueActionGroupManager>();
    }
    
    public static FightActionGroupManager getInstance() {
        return FightActionGroupManager.m_instance;
    }
    
    public ActionGroup addActionToPendingGroup(final Fight fight, final Action action) {
        if (fight == null || action == null) {
            return null;
        }
        return this.addActionToPendingGroup(fight.getId(), action);
    }
    
    public ActionGroup addActionToPendingGroup(final int fightId, final Action action) {
        final QueueActionGroupManager manager = this.getOrCreateManager(fightId);
        return manager.addActionToPendingGroup(action);
    }
    
    public void executePendingGroup(final FightInfo fight) {
        if (fight == null) {
            return;
        }
        this.executePendingGroup(fight.getId());
    }
    
    public void executePendingGroup(final int fightId) {
        final QueueActionGroupManager manager = this.getOrCreateManager(fightId);
        manager.executePendingGroup();
    }
    
    private QueueActionGroupManager getOrCreateManager(final int fightId) {
        QueueActionGroupManager manager = this.m_managers.get(fightId);
        if (manager == null) {
            manager = new QueueActionGroupManager();
            this.m_managers.put(fightId, manager);
        }
        return manager;
    }
    
    public void executeAllActionInPendingGroup(final int fightId) {
        final QueueActionGroupManager manager = this.m_managers.get(fightId);
        if (manager != null) {
            manager.executeAllAction();
        }
        this.m_managers.remove(fightId);
    }
    
    public void removePendingGroups(final int fightId) {
        final QueueActionGroupManager manager = this.m_managers.remove(fightId);
        if (manager != null) {
            manager.killActionsToPendingGroup();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightActionGroupManager.class);
        m_instance = new FightActionGroupManager();
    }
}
