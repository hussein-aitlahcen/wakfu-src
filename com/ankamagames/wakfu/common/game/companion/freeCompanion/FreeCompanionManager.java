package com.ankamagames.wakfu.common.game.companion.freeCompanion;

import java.util.*;

public final class FreeCompanionManager
{
    public static final FreeCompanionManager INSTANCE;
    private short m_freeCompanionBreedId;
    private final List<FreeCompanionManagerListener> m_listeners;
    
    private FreeCompanionManager() {
        super();
        this.m_freeCompanionBreedId = -1;
        this.m_listeners = new ArrayList<FreeCompanionManagerListener>();
    }
    
    public void setFreeCompanionBreedId(final short freeCompanionBreedId) {
        this.m_freeCompanionBreedId = freeCompanionBreedId;
        this.fireFreeCompanionBreedIdChanged();
    }
    
    public boolean isFreeCompanion(final short companionBreedId) {
        return this.m_freeCompanionBreedId == companionBreedId;
    }
    
    public short getFreeCompanionBreedId() {
        return this.m_freeCompanionBreedId;
    }
    
    public boolean hasFreeCompanion() {
        return this.m_freeCompanionBreedId > 0;
    }
    
    public void addListener(final FreeCompanionManagerListener listener) {
        if (!this.m_listeners.contains(listener)) {
            this.m_listeners.add(listener);
        }
    }
    
    public void removeListener(final FreeCompanionManagerListener listener) {
        this.m_listeners.remove(listener);
    }
    
    private void fireFreeCompanionBreedIdChanged() {
        if (this.m_listeners.isEmpty()) {
            return;
        }
        final ArrayList<FreeCompanionManagerListener> listeners = new ArrayList<FreeCompanionManagerListener>(this.m_listeners);
        for (final FreeCompanionManagerListener listener : listeners) {
            listener.onFreeCompanionBreedIdChanged();
        }
    }
    
    @Override
    public String toString() {
        return "FreeCompanionManager{m_freeCompanionBreedId=" + this.m_freeCompanionBreedId + '}';
    }
    
    static {
        INSTANCE = new FreeCompanionManager();
    }
}
