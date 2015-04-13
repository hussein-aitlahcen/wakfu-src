package com.ankamagames.baseImpl.common.clientAndServer.game.characteristic;

import java.util.*;

public abstract class AbstractCharacteristic
{
    private List<CharacteristicUpdateListener> m_listeners;
    
    public AbstractCharacteristic() {
        super();
        this.m_listeners = null;
    }
    
    public abstract int value();
    
    public abstract int unboundedValue();
    
    public abstract int max();
    
    public abstract int plainMax();
    
    public abstract int min();
    
    public abstract int updateMaxPercentModifier(final int p0);
    
    public abstract void set(final int p0);
    
    public abstract int add(final int p0);
    
    public abstract int substract(final int p0);
    
    public abstract int updateMaxValue(final int p0);
    
    public abstract int updateMinValue(final int p0);
    
    public abstract void setMax(final int p0);
    
    public abstract void setMin(final int p0);
    
    public abstract void makeDefault();
    
    public boolean isPositive() {
        return this.value() > 0;
    }
    
    public boolean isNegative() {
        return this.value() < 0;
    }
    
    public boolean isZero() {
        return this.value() == 0;
    }
    
    public boolean isNonZero() {
        return this.value() != 0;
    }
    
    public boolean isMaximum() {
        return this.unboundedValue() >= this.max();
    }
    
    public boolean isMinimal() {
        return this.unboundedValue() <= this.min();
    }
    
    public void toMax() {
        this.set(this.max());
    }
    
    public void toMin() {
        this.set(this.min());
    }
    
    public abstract CharacteristicType getType();
    
    public void setListeners(final List<CharacteristicUpdateListener> listeners) {
        if (listeners == null) {
            return;
        }
        if (this.m_listeners == null) {
            (this.m_listeners = new ArrayList<CharacteristicUpdateListener>()).addAll(listeners);
        }
        else {
            this.clearListener();
            this.m_listeners.addAll(listeners);
        }
    }
    
    public void addListener(final CharacteristicUpdateListener listener) {
        if (this.m_listeners == null) {
            (this.m_listeners = new ArrayList<CharacteristicUpdateListener>()).add(listener);
        }
        else if (!this.m_listeners.contains(listener)) {
            this.m_listeners.add(listener);
        }
    }
    
    public void removeListener(final CharacteristicUpdateListener listener) {
        if (this.m_listeners != null) {
            this.m_listeners.remove(listener);
        }
    }
    
    public List<CharacteristicUpdateListener> getListeners() {
        return this.m_listeners;
    }
    
    public void clearListener() {
        if (this.m_listeners != null) {
            this.m_listeners.clear();
        }
    }
    
    public void dispatchUpdate() {
        if (this.m_listeners != null && !this.m_listeners.isEmpty()) {
            for (final CharacteristicUpdateListener listener : this.m_listeners) {
                listener.onCharacteristicUpdated(this);
            }
        }
    }
}
