package com.ankamagames.wakfu.common.game.interactiveElements.param;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.chaos.*;
import java.util.*;

public class IEDestructibleParameter extends IEParameter
{
    public static final short STATE_ALIVE = 0;
    public static final short STATE_HIT = 1;
    public static final short STATE_DESTROYED = 2;
    private final int m_pdv;
    private final long m_regenDelay;
    private final int m_resWater;
    private final int m_resFire;
    private final int m_resEarth;
    private final int m_resWind;
    private final ArrayList<WakfuEffect> m_effects;
    
    public IEDestructibleParameter(final int paramId, final int pdv, final long regenDelay, final int resWater, final int resFire, final int resEarth, final int resWind) {
        super(paramId, 0, ChaosInteractiveCategory.NO_CHAOS, 0);
        this.m_effects = new ArrayList<WakfuEffect>();
        this.m_pdv = pdv;
        this.m_regenDelay = regenDelay;
        this.m_resWater = resWater;
        this.m_resFire = resFire;
        this.m_resEarth = resEarth;
        this.m_resWind = resWind;
    }
    
    public int getPdv() {
        return this.m_pdv;
    }
    
    public long getRegenDelay() {
        return this.m_regenDelay;
    }
    
    public boolean mustRegen() {
        return this.m_regenDelay >= 0L;
    }
    
    public int getResWater() {
        return this.m_resWater;
    }
    
    public int getResFire() {
        return this.m_resFire;
    }
    
    public int getResEarth() {
        return this.m_resEarth;
    }
    
    public int getResWind() {
        return this.m_resWind;
    }
    
    public void addEffect(final WakfuEffect effect) {
        this.m_effects.add(effect);
    }
    
    public Iterator<WakfuEffect> effectsIterator() {
        return this.m_effects.iterator();
    }
}
