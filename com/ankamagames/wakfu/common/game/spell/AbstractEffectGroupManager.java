package com.ankamagames.wakfu.common.game.spell;

import org.apache.log4j.*;
import gnu.trove.*;

public abstract class AbstractEffectGroupManager<G extends AbstractEffectGroup>
{
    protected static final Logger m_logger;
    private final TIntObjectHashMap<G> m_effectGroups;
    private static AbstractEffectGroupManager<? extends AbstractEffectGroup> m_instance;
    
    protected AbstractEffectGroupManager() {
        super();
        this.m_effectGroups = new TIntObjectHashMap<G>();
    }
    
    public void addEffectGroup(final G effectGroup) {
        this.m_effectGroups.put(effectGroup.getEffectGroupBaseId(), effectGroup);
    }
    
    public G getEffectGroup(final int effectGroupId) {
        return this.m_effectGroups.get(effectGroupId);
    }
    
    public static AbstractEffectGroupManager<? extends AbstractEffectGroup> getInstance() {
        return AbstractEffectGroupManager.m_instance;
    }
    
    public static void setAbstractEffectGroupManager(final AbstractEffectGroupManager<? extends AbstractEffectGroup> manager) {
        AbstractEffectGroupManager.m_instance = manager;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractEffectGroupManager.class);
    }
}
