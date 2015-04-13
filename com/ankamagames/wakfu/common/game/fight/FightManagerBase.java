package com.ankamagames.wakfu.common.game.fight;

import org.apache.log4j.*;
import gnu.trove.*;

public abstract class FightManagerBase<F extends BasicFight>
{
    protected static final Logger m_logger;
    protected final TIntObjectHashMap<F> m_fights;
    
    public FightManagerBase() {
        super();
        this.m_fights = new TIntObjectHashMap<F>();
    }
    
    public void addFight(final F fight) {
        this.m_fights.put(fight.getId(), fight);
    }
    
    public void removeFight(final F fight) {
        this.m_fights.remove(fight.getId());
    }
    
    public F getFightFromId(final int id) {
        return this.m_fights.get(id);
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightManagerBase.class);
    }
}
