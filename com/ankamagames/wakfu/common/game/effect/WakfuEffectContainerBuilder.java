package com.ankamagames.wakfu.common.game.effect;

import java.util.*;

public final class WakfuEffectContainerBuilder
{
    private long m_containerId;
    private WakfuEffect m_effect;
    private int m_containerType;
    private short m_level;
    private short m_aggroWeight;
    private short m_allyEfficacity;
    private short m_foeEfficacity;
    private boolean m_isFlagged;
    private BitSet m_flags;
    
    public WakfuEffectContainerBuilder() {
        super();
        this.m_level = 0;
        this.m_aggroWeight = 0;
        this.m_allyEfficacity = 0;
        this.m_foeEfficacity = 0;
        this.m_isFlagged = false;
        this.m_flags = null;
    }
    
    public WakfuEffectContainerBuilder setContainerId(final long containerId) {
        this.m_containerId = containerId;
        return this;
    }
    
    public WakfuEffectContainerBuilder setContainerType(final int containerType) {
        this.m_containerType = containerType;
        return this;
    }
    
    public WakfuEffectContainerBuilder setEffect(final WakfuEffect effect) {
        this.m_effect = effect;
        return this;
    }
    
    public WakfuEffectContainerBuilder setAggroWeight(final short aggroWeight) {
        this.m_aggroWeight = aggroWeight;
        return this;
    }
    
    public WakfuEffectContainerBuilder setAllyEfficacity(final short allyEfficacity) {
        this.m_allyEfficacity = allyEfficacity;
        return this;
    }
    
    public WakfuEffectContainerBuilder setFoeEfficacity(final short foeEfficacity) {
        this.m_foeEfficacity = foeEfficacity;
        return this;
    }
    
    public WakfuEffectContainerBuilder setLevel(final short level) {
        this.m_level = level;
        return this;
    }
    
    public WakfuEffectContainer build() {
        final WakfuEffectContainerImpl container = new WakfuEffectContainerImpl(this.m_containerId, this.m_containerType, this.m_effect);
        container.m_level = this.m_level;
        container.m_aggroWeight = this.m_aggroWeight;
        container.m_allyEfficacity = this.m_allyEfficacity;
        container.m_foeEfficacity = this.m_foeEfficacity;
        return container;
    }
    
    private static class WakfuEffectContainerImpl implements WakfuEffectContainer
    {
        private final long m_containerId;
        private final WakfuEffect m_effect;
        private final int m_containerType;
        private short m_level;
        private short m_aggroWeight;
        private short m_allyEfficacity;
        private short m_foeEfficacity;
        
        private WakfuEffectContainerImpl(final long containerId, final int containerType, final WakfuEffect effect) {
            super();
            this.m_level = 0;
            this.m_aggroWeight = 0;
            this.m_allyEfficacity = 0;
            this.m_foeEfficacity = 0;
            this.m_containerId = containerId;
            this.m_containerType = containerType;
            this.m_effect = effect;
        }
        
        @Override
        public short getLevel() {
            return this.m_level;
        }
        
        @Override
        public short getAggroWeight() {
            return this.m_aggroWeight;
        }
        
        @Override
        public short getAllyEfficacity() {
            return this.m_allyEfficacity;
        }
        
        @Override
        public short getFoeEfficacity() {
            return this.m_foeEfficacity;
        }
        
        @Override
        public int getContainerType() {
            return this.m_containerType;
        }
        
        @Override
        public long getEffectContainerId() {
            return this.m_containerId;
        }
        
        @Override
        public Iterator<WakfuEffect> iterator() {
            return Collections.singleton(this.m_effect).iterator();
        }
    }
}
