package com.ankamagames.wakfu.common.game.protector;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import gnu.trove.*;

public final class ProtectorBuffManager
{
    public static final ProtectorBuffManager INSTANCE;
    protected static final Logger m_logger;
    private final TIntObjectHashMap<ProtectorBuffList> m_lists;
    private final TIntObjectHashMap<WakfuStandardEffect> m_effects;
    private final TIntObjectHashMap<ProtectorBuff> m_buffs;
    private ProtectorBuffFactory m_buffFactory;
    
    private ProtectorBuffManager() {
        super();
        this.m_lists = new TIntObjectHashMap<ProtectorBuffList>();
        this.m_effects = new TIntObjectHashMap<WakfuStandardEffect>();
        this.m_buffs = new TIntObjectHashMap<ProtectorBuff>();
        this.m_buffFactory = new ProtectorBuffFactory<ProtectorBuff>() {
            @Override
            public ProtectorBuff createBuff(final int id, final SimpleCriterion criterion, final byte origin, final ArrayList<WakfuStandardEffect> effects) {
                return new ProtectorBuff(id, criterion, origin, effects);
            }
        };
    }
    
    public void setBuffFactory(final ProtectorBuffFactory buffFactory) {
        this.m_buffFactory = buffFactory;
    }
    
    public void addEffect(final WakfuStandardEffect effect) {
        if (effect != null) {
            this.m_effects.put(effect.getEffectId(), effect);
        }
    }
    
    public WakfuStandardEffect getEffect(final int effectId) {
        return this.m_effects.get(effectId);
    }
    
    public ProtectorBuff addBuff(final int buffId, final SimpleCriterion criterion, final byte origin, final int[] effectIds) {
        final ArrayList<WakfuStandardEffect> effects = new ArrayList<WakfuStandardEffect>();
        if (effectIds != null) {
            for (final int effectId : effectIds) {
                final WakfuStandardEffect effect = this.m_effects.get(effectId);
                if (effect != null) {
                    effects.add(effect);
                }
                else {
                    ProtectorBuffManager.m_logger.error((Object)("L'effet n'existe pas (ID=" + effectId + "), ou n'a pas pu etre charg\u00e9 pour le buff ID=" + buffId));
                }
            }
        }
        final ProtectorBuff buff = this.m_buffFactory.createBuff(buffId, criterion, origin, effects);
        this.m_buffs.put(buffId, buff);
        return buff;
    }
    
    public ProtectorBuff getBuff(final int buffId) {
        return this.m_buffs.get(buffId);
    }
    
    public void addBuffList(final int listId, final int[] buffIds) {
        final ProtectorBuffList list = new ProtectorBuffList(listId, buffIds);
        this.m_lists.put(listId, list);
    }
    
    public ProtectorBuffList getBuffList(final int buffListId) {
        return this.m_lists.get(buffListId);
    }
    
    public TIntObjectIterator<ProtectorBuff> getBuffs() {
        return this.m_buffs.iterator();
    }
    
    static {
        INSTANCE = new ProtectorBuffManager();
        m_logger = Logger.getLogger((Class)ProtectorBuffManager.class);
    }
}
