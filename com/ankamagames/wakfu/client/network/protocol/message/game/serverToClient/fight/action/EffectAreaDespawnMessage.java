package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import java.nio.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class EffectAreaDespawnMessage extends AbstractFightActionMessage
{
    private long m_effectAreaId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer byteBuffer = ByteBuffer.wrap(rawDatas);
        this.decodeFightActionHeader(byteBuffer);
        this.m_effectAreaId = byteBuffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 6204;
    }
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return FightActionType.EFFECT_AREA_DESPAWN;
    }
    
    public long getEffectAreaId() {
        return this.m_effectAreaId;
    }
}
