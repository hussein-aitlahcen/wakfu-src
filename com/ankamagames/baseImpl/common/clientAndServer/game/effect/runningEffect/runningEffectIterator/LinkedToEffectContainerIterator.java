package com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.runningEffectIterator;

import java.util.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import gnu.trove.*;

public class LinkedToEffectContainerIterator implements Iterator<RunningEffect>
{
    protected static final Logger m_logger;
    private RunningEffectManager m_runningEffectManagerCreator;
    private boolean m_nextChecked;
    private TLongObjectIterator<RunningEffect> m_iterator;
    private RunningEffect m_currentRunningEffect;
    private EffectContainer m_container;
    
    public LinkedToEffectContainerIterator(final RunningEffectManager creator, final TLongObjectIterator<RunningEffect> effects, final EffectContainer container) {
        super();
        if (effects == null) {
            throw new UnsupportedOperationException("checkOut d'un iterator sans liste derri\u00e8re");
        }
        if (container == null) {
            throw new UnsupportedOperationException("checkOut d'un  LinkedToEffectContainerIterator sans container");
        }
        this.m_runningEffectManagerCreator = creator;
        this.m_iterator = effects;
        this.m_container = container;
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
            if (this.m_currentRunningEffect.getEffectContainer() != null && this.m_currentRunningEffect.getEffectContainer().getContainerType() == this.m_container.getContainerType() && this.m_currentRunningEffect.getEffectContainer().getEffectContainerId() == this.m_container.getEffectContainerId()) {
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
        this.m_runningEffectManagerCreator.onEffectRemoved(this.m_currentRunningEffect);
    }
    
    static {
        m_logger = Logger.getLogger((Class)LinkedToEffectContainerIterator.class);
    }
}
