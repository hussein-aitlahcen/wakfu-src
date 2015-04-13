package com.ankamagames.wakfu.client.core.game.characterInfo.breedSpecific;

import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;

public class EcaflipFightListenerManager
{
    public static final EcaflipFightListenerManager INSTANCE;
    private final TLongObjectHashMap<EcaflipFightListener> m_listeners;
    
    private EcaflipFightListenerManager() {
        super();
        this.m_listeners = new TLongObjectHashMap<EcaflipFightListener>();
    }
    
    public EcaflipFightListener getListener(final long id) {
        return this.m_listeners.get(id);
    }
    
    public void registerCharacter(final PlayerCharacter info) {
        if (this.m_listeners.contains(info.getId())) {
            return;
        }
        final EcaflipFightListener value = new EcaflipFightListener(info);
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
        sb.append("EcaflipFightListenerManager");
        sb.append("{}");
        return sb.toString();
    }
    
    static {
        INSTANCE = new EcaflipFightListenerManager();
    }
}
