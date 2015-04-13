package com.ankamagames.wakfu.common.game.effect.runningEffect.runningEffectWakfuIterator;

import java.util.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import gnu.trove.*;

public class LinkedToItemIterator implements Iterator<RunningEffect>
{
    protected static final Logger m_logger;
    private final RunningEffectManager m_runningEffectManager;
    private final TLongObjectIterator<RunningEffect> m_iterator;
    private RunningEffect m_currentRunningEffect;
    private final Item m_item;
    private boolean m_nextChecked;
    
    public LinkedToItemIterator(final RunningEffectManager creator, final TLongObjectIterator<RunningEffect> effects, final Item item) {
        super();
        this.m_runningEffectManager = creator;
        this.m_iterator = effects;
        this.m_item = item;
        if (effects != null && item != null) {
            return;
        }
        if (effects == null) {
            throw new UnsupportedOperationException("checkOut d'un iterator sans liste derri\u00e8re");
        }
        throw new UnsupportedOperationException("checkOut d'un LinkedToItemIterator sans item");
    }
    
    @Override
    public boolean hasNext() {
        this.m_nextChecked = true;
        if (!this.m_iterator.hasNext()) {
            return false;
        }
        while (this.m_iterator.hasNext()) {
            this.m_iterator.advance();
            this.m_currentRunningEffect = this.m_iterator.value();
            final EffectContainer effectContainer = this.m_currentRunningEffect.getEffectContainer();
            if (effectContainer == null) {
                continue;
            }
            if (effectContainer.getContainerType() != 12) {
                continue;
            }
            if (!(effectContainer instanceof Item)) {
                continue;
            }
            if (((Item)effectContainer).getUniqueId() == this.m_item.getUniqueId()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public RunningEffect next() {
        if (!this.m_nextChecked && !this.hasNext()) {
            throw new UnsupportedOperationException("Depassement de liste, cause probable : appel de next() sans v\u00e9rification");
        }
        this.m_nextChecked = false;
        return this.m_currentRunningEffect;
    }
    
    @Override
    public void remove() {
        this.m_iterator.remove();
        this.m_runningEffectManager.onEffectRemoved(this.m_currentRunningEffect);
    }
    
    static {
        m_logger = Logger.getLogger((Class)LinkedToItemIterator.class);
    }
}
