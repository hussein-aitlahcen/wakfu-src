package com.ankamagames.wakfu.client.core.game.characterInfo.breedSpecific;

import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;

public class RoublardFightListenerManager
{
    public static final RoublardFightListenerManager INSTANCE;
    private final TLongObjectHashMap<RoublardFightListener> m_listeners;
    
    private RoublardFightListenerManager() {
        super();
        this.m_listeners = new TLongObjectHashMap<RoublardFightListener>();
    }
    
    public RoublardFightListener getListener(final long id) {
        return this.m_listeners.get(id);
    }
    
    public void registerCharacter(final PlayerCharacter info) {
        if (this.m_listeners.contains(info.getId())) {
            return;
        }
        final RoublardFightListener value = new RoublardFightListener();
        this.m_listeners.put(info.getId(), value);
        info.setBreedSpecific(value);
    }
    
    public void unregisterCharacter(final Validable info) {
        this.m_listeners.remove(info.getId());
    }
    
    public void clear() {
        this.m_listeners.clear();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("RoublardFightListenerManager");
        sb.append("{}");
        return sb.toString();
    }
    
    static {
        INSTANCE = new RoublardFightListenerManager();
    }
}
