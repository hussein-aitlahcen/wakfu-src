package com.ankamagames.wakfu.common.game.effect.runningEffect.util.summonDouble;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;

public abstract class DoubleSpellsFinder
{
    protected static final Logger m_logger;
    
    public abstract SpellInventory<AbstractSpellLevel> getSpells(final BasicCharacterInfo p0, final SummonDoubleParams p1, final short p2);
    
    static {
        m_logger = Logger.getLogger((Class)DoubleSpellsFinder.class);
    }
}
