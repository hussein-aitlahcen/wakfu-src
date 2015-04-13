package com.ankamagames.wakfu.client.core.game.characterInfo.breedSpecific;

import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;

public class PandawaFightListenerManager
{
    public static final PandawaFightListenerManager INSTANCE;
    private final TLongObjectHashMap<PandawaFightListener> m_listeners;
    
    private PandawaFightListenerManager() {
        super();
        this.m_listeners = new TLongObjectHashMap<PandawaFightListener>();
    }
    
    public void registerCharacter(final PlayerCharacter info) {
        if (this.m_listeners.contains(info.getId())) {
            return;
        }
        final PandawaFightListener value = new PandawaFightListener(info);
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
        sb.append("PandawaFightListenerManager");
        sb.append("{}");
        return sb.toString();
    }
    
    static {
        INSTANCE = new PandawaFightListenerManager();
    }
}
