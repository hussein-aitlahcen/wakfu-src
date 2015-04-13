package com.ankamagames.wakfu.client.core.game.characterInfo.breedSpecific;

import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;

public class XelorFightListenerManager
{
    public static final XelorFightListenerManager INSTANCE;
    private final TLongObjectHashMap<XelorFightListener> m_listeners;
    
    private XelorFightListenerManager() {
        super();
        this.m_listeners = new TLongObjectHashMap<XelorFightListener>();
    }
    
    public XelorFightListener getListener(final long id) {
        return this.m_listeners.get(id);
    }
    
    public void registerCharacter(final PlayerCharacter info) {
        if (this.m_listeners.contains(info.getId())) {
            return;
        }
        final XelorFightListener value = new XelorFightListener(info);
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
        return "XelorFightListenerManager{m_listeners=" + this.m_listeners + '}';
    }
    
    static {
        INSTANCE = new XelorFightListenerManager();
    }
}
