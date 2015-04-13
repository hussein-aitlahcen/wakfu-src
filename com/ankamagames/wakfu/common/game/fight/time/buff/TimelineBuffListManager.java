package com.ankamagames.wakfu.common.game.fight.time.buff;

import org.apache.log4j.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class TimelineBuffListManager
{
    private static final Logger m_logger;
    public static final TimelineBuffListManager INSTANCE;
    private final TIntIntHashMap m_gfx;
    private final Map<TimelineBuffType, TIntHashSet> m_effectsByType;
    private final TIntIntHashMap m_effectsByBaseId;
    private final TIntObjectHashMap<WakfuEffect> m_effects;
    private final HashSet<WakfuEffect> m_playerEffects;
    
    private TimelineBuffListManager() {
        super();
        this.m_gfx = new TIntIntHashMap();
        this.m_effectsByType = new HashMap<TimelineBuffType, TIntHashSet>();
        this.m_effectsByBaseId = new TIntIntHashMap();
        this.m_effects = new TIntObjectHashMap<WakfuEffect>();
        this.m_playerEffects = new HashSet<WakfuEffect>();
    }
    
    public void setType(final TimelineBuffType type, final int effectId) {
        TIntHashSet typeSet = this.m_effectsByType.get(type);
        if (typeSet == null) {
            typeSet = new TIntHashSet();
            this.m_effectsByType.put(type, typeSet);
        }
        typeSet.add(effectId);
    }
    
    public boolean isOfType(final TimelineBuffType type, final int effectId) {
        final TIntHashSet typeSet = this.m_effectsByType.get(type);
        return typeSet != null && typeSet.contains(effectId);
    }
    
    public void addGfx(final int effectId, final int gfxId) {
        if (this.m_gfx.put(effectId, gfxId) != 0) {
            TimelineBuffListManager.m_logger.error((Object)("Ajout multiple du gfx de buff de v\u00e9locit\u00e9 " + effectId), (Throwable)new UnsupportedOperationException());
        }
    }
    
    public int getGfx(final int effectId) {
        return this.m_gfx.get(effectId);
    }
    
    public void addEffect(final WakfuEffect effect, final int baseId) {
        this.m_effects.put(effect.getEffectId(), effect);
        this.m_effectsByBaseId.put(baseId, effect.getEffectId());
    }
    
    public List<WakfuEffect> getEffects() {
        final WakfuEffect[] res = new WakfuEffect[this.m_effects.size()];
        this.m_effects.getValues(res);
        return Arrays.asList(res);
    }
    
    public WakfuEffect getEffectById(final int effectId) {
        return this.m_effects.get(effectId);
    }
    
    public WakfuEffect getEffectByBaseId(final int baseId) {
        return this.m_effects.get(this.m_effectsByBaseId.get(baseId));
    }
    
    public static WakfuEffectContainer getContainerForEffect(final long effectContainerId, final WakfuEffect effect) {
        return new TimelineBuffListEffectContainer(effectContainerId, effect);
    }
    
    public void addPlayerEffect(final WakfuEffect effect) {
        this.m_playerEffects.add(effect);
    }
    
    public HashSet<WakfuEffect> getPlayerEffects() {
        return this.m_playerEffects;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TimelineBuffListManager.class);
        INSTANCE = new TimelineBuffListManager();
    }
}
