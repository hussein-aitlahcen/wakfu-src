package com.ankamagames.wakfu.client.core.game.protector.event;

import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.game.protector.snapshot.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class ProtectorChaosEndedEvent extends ProtectorEvent
{
    private Territory m_territory;
    
    public void setTerritory(final Territory territory) {
        this.m_territory = territory;
    }
    
    public Territory getTerritory() {
        return this.m_territory;
    }
    
    @Override
    public ProtectorMood getProtectorMood() {
        return ProtectorMood.HAPPY;
    }
    
    @Override
    public String[] getParams() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return null;
        }
        final String[] params = new String[2];
        String territoryName = null;
        if (WakfuTranslator.getInstance().containsContentKey(66, this.m_territory.getId())) {
            territoryName = WakfuTranslator.getInstance().getString(66, this.m_territory.getId(), new Object[0]);
        }
        params[0] = String.valueOf(territoryName);
        params[1] = String.valueOf((this.m_territory.getId() == localPlayer.getCurrentTerritoryId()) ? 1 : 0);
        return params;
    }
}
