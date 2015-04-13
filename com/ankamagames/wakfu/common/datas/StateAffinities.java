package com.ankamagames.wakfu.common.datas;

import gnu.trove.*;
import java.util.*;

final class StateAffinities
{
    private final TShortIntHashMap m_resistances;
    private final TShortIntHashMap m_applicationBonus;
    private List<StateAffinitiesUpdateListener> m_updateListeners;
    
    StateAffinities() {
        super();
        this.m_resistances = new TShortIntHashMap();
        this.m_applicationBonus = new TShortIntHashMap();
        this.m_updateListeners = null;
    }
    
    public void addStateResistance(final short stateId, final int value) {
        final int newValue = this.m_resistances.adjustOrPutValue(stateId, value, value);
        if (this.m_updateListeners != null) {
            for (int i = 0, size = this.m_updateListeners.size(); i < size; ++i) {
                this.m_updateListeners.get(i).onStateResistanceUpdate(stateId, newValue);
            }
        }
    }
    
    public int getStateResistance(final short stateId) {
        return this.m_resistances.get(stateId);
    }
    
    public void addApplicationBonus(final short stateId, final int value) {
        final int newValue = this.m_applicationBonus.adjustOrPutValue(stateId, value, value);
        if (this.m_updateListeners != null) {
            for (int i = 0, size = this.m_updateListeners.size(); i < size; ++i) {
                this.m_updateListeners.get(i).onStateApplicationUpdate(stateId, newValue);
            }
        }
    }
    
    public int getApplicationBonus(final short stateId) {
        return this.m_applicationBonus.get(stateId);
    }
    
    public void addUpdateListener(final StateAffinitiesUpdateListener listener) {
        if (this.m_updateListeners == null) {
            this.m_updateListeners = new ArrayList<StateAffinitiesUpdateListener>(2);
        }
        this.m_updateListeners.add(listener);
    }
    
    public void removeUpdateListener(final StateAffinitiesUpdateListener listener) {
        if (this.m_updateListeners == null) {
            return;
        }
        this.m_updateListeners.remove(listener);
    }
    
    public void clear() {
        this.m_resistances.clear();
        this.m_applicationBonus.clear();
        this.m_updateListeners = null;
    }
}
