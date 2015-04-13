package com.ankamagames.wakfu.common.game.fight;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fight.fightHistory.*;
import gnu.trove.*;

public abstract class FightHistory
{
    protected static final Logger m_logger;
    protected final TLongObjectHashMap<PlayerFightHistory> m_playerHistories;
    private TLongIntHashMap m_collectedKamas;
    
    public FightHistory() {
        super();
        this.m_playerHistories = new TLongObjectHashMap<PlayerFightHistory>();
        this.m_collectedKamas = new TLongIntHashMap();
    }
    
    public void setCollectedKamas(final TLongIntHashMap collectedKamas) {
        this.m_collectedKamas = collectedKamas;
    }
    
    public TLongIntHashMap getCollectedKamas() {
        return this.m_collectedKamas;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightHistory.class);
    }
}
