package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.*;

public class HealthSynchronizationAction extends TimedAction
{
    private final int m_health;
    private final int m_healthRegen;
    
    public HealthSynchronizationAction(final CharacterHealthUpdateMessage message) {
        super(TimedAction.getNextUid(), FightActionType.HEALTH_SYNC.getId(), 0);
        this.m_health = message.getValue();
        this.m_healthRegen = message.getHealthRegen();
    }
    
    @Override
    protected long onRun() {
        WakfuGameEntity.getInstance().getLocalPlayer().getHpRegenHandler().synchronizeValue(this.m_health, this.m_healthRegen);
        return 0L;
    }
    
    @Override
    protected void onActionFinished() {
    }
}
