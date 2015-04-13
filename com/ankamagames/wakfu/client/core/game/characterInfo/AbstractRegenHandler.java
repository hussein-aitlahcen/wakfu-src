package com.ankamagames.wakfu.client.core.game.characterInfo;

import org.apache.log4j.*;
import java.util.*;

public abstract class AbstractRegenHandler
{
    private static final Logger m_logger;
    private final ArrayList<RegenListener> m_listeners;
    private final double m_regenTickDuration;
    private int m_regen;
    private long m_regenerationReferenceTime;
    private int m_regenerationReferenceValue;
    private int m_regenerationCumulFromReferenceTime;
    
    protected AbstractRegenHandler(final double regenTickDuration) {
        super();
        this.m_listeners = new ArrayList<RegenListener>(2);
        this.m_regenTickDuration = regenTickDuration;
    }
    
    public void initializeRegeneration() {
        this.m_regenerationReferenceTime = System.currentTimeMillis();
        this.m_regenerationReferenceValue = this.getValue();
        this.m_regenerationCumulFromReferenceTime = 0;
    }
    
    public void synchronizeValue(final int newValue, final int newRegen) {
        this.onValueSet(newValue);
        this.m_regen = newRegen;
        this.m_regenerationReferenceTime = System.currentTimeMillis();
        this.m_regenerationReferenceValue = newValue;
        this.m_regenerationCumulFromReferenceTime = 0;
    }
    
    public void addRegenListener(final RegenListener l) {
        this.m_listeners.add(l);
    }
    
    public void removeRegenListener(final RegenListener l) {
        this.m_listeners.remove(l);
    }
    
    public void setCustomRegen(final int regen) {
        this.m_regen = regen;
    }
    
    public void onRegenTick(final long realTime) {
        final double delay = realTime - this.m_regenerationReferenceTime;
        final int regen = (int)Math.round(this.m_regen * delay / this.m_regenTickDuration);
        if (regen > this.m_regenerationCumulFromReferenceTime) {
            final int gain = regen - this.m_regenerationCumulFromReferenceTime;
            this.onRegen(gain);
            this.m_regenerationCumulFromReferenceTime = regen;
        }
    }
    
    protected abstract void onValueSet(final int p0);
    
    protected void onRegen(final int gain) {
        for (int i = this.m_listeners.size() - 1; i >= 0; --i) {
            this.m_listeners.get(i).onRegen(gain);
        }
    }
    
    protected abstract int getValue();
    
    static {
        m_logger = Logger.getLogger((Class)AbstractRegenHandler.class);
    }
}
